import Ember from "ember";
import ReunionServiceInjected from "../../mixins/reunion-service-injected";
import TemaServiceInjected from "../../mixins/tema-service-injected";
import DuracionesServiceInjected from "../../mixins/duraciones-service-injected";
import Tema from "../../concepts/tema";
import NavigatorInjected from "../../mixins/navigator-injected";
import estadoDeReunion from "./estadoDeReunion";

export default Ember.Controller.extend(ReunionServiceInjected, TemaServiceInjected, DuracionesServiceInjected, NavigatorInjected, {

  esObligatorio: false,
  mostrarObligatorios: false,
  visibilidadCardDeTema: false,

  fechaDeReunion: Ember.computed('model.reunion.fecha', function () {
      let fecha = this.get('model.reunion.fecha');
      return moment(fecha).format('DD-MM-YYYY');
    }
  ),

  resaltarPropuesas: Ember.computed('mostrarObligatorios', function () {
    return this._resaltarCuando(!this.mostrarObligatorios);
  }),

  resaltarObligatoriedad: Ember.computed('mostrarObligatorios', function () {
    return this._resaltarCuando(this.mostrarObligatorios);
  }),

  contarPropuestasNoObligatorias: Ember.computed('model.reunion', 'mostrarObligatorios', function () {
    return this._contarReunionesCon("NO_OBLIGATORIO");
  }),

  contarPropuestasObligatorias: Ember.computed('model.reunion', 'mostrarObligatorios', function () {
    return this._contarReunionesCon("OBLIGATORIO");
  }),

  reunion: Ember.computed('model.reunion', function () {
    return this.get('model.reunion');
  }),

  nombreDeDuraciones:
    Ember.computed('duraciones', function () {
      return this.get('duraciones').map(function (duracion) {
        return duracion.nombre;
      });
    }),
  temasOrdenados:
    Ember.computed('reunion.temasPropuestos', function () {
      let todosLosTemas = this.get('reunion.temasPropuestos');
      let temasOrdenados = todosLosTemas.filter(function (tema) {
        return tema.obligatoriedad === "OBLIGATORIO_GENERAL";
      });
      temasOrdenados = temasOrdenados.concat(todosLosTemas.filter(function (tema) {
        return tema.obligatoriedad === "OBLIGATORIO";
      }));
      temasOrdenados = temasOrdenados.concat(todosLosTemas.filter(function (tema) {
        return tema.obligatoriedad === "NO_OBLIGATORIO";
      }));
      return temasOrdenados;
    }),

  temasVotados: Ember.computed('reunion.temaPropuestos', 'reunion.temasVotados', function () {
    let usuarioId = this.model.usuarioActual.id;
    let todosLosTemas = this.get('reunion.temasPropuestos');
    let temasVotados = todosLosTemas.filter(function (tema) {
      return tema.idsDeInteresados.includes(usuarioId);
    });
    return this._temasVotadosPorElUsuario(temasVotados, usuarioId);
  }),

  _temasVotadosPorElUsuario: function (temasVotados, usuarioId) {
    let temasVotadosPorElUsuario = [];
    let self = this;

    temasVotados.forEach(function (tema) {
      temasVotadosPorElUsuario.push(tema);

      let votosRepetidos = self._votosRepetidosPor(tema, usuarioId);

      while (votosRepetidos > 0) {
        temasVotadosPorElUsuario.push(tema);
        votosRepetidos--;
      }
    });

    return temasVotadosPorElUsuario;
  },

  _votosRepetidosPor: function (tema, usuarioId) {
    return tema.idsDeInteresados.filter(function (id) {
      return id === usuarioId;
    }).length - 1;
  },

  votosOpacity: Ember.computed('updatingVotos', function () {
    if (this.get('updatingVotos')) {
      return "opacity-35";
    }
  }),

  votarDisabled: Ember.computed('votarDisabled', 'updatingVotos', function () {
    if (this.get('updatingVotos')) {
      return "disabled";
    }
  }),

  estaCerrada:
    Ember.computed('reunion.status', function () {
      return ((this.get('reunion.status') === estadoDeReunion.CERRADA) || (this.get('reunion.status') === estadoDeReunion.CON_MINUTA));
    }),

  usuarioActual:
    Ember.computed('model.usuarioActual', function () {
      return this.get('model.usuarioActual');
    }),

  mostrandoFecha:
    Ember.computed('editandoFecha', function () {
      return !this.get('editandoFecha');
    }),

  fechaObserver:
    Ember.observer('reunion.fecha', function () {
      if (this.get('editandoFecha')) {
        this.set('editandoFecha', false);
        this._guardarCambios();
      }
    }),
  votosRestantes:
    Ember.computed('reunion.temasPropuestos.@each.cantidadVotosPropios', function () {
      var temas = this.get('reunion.temasPropuestos');
      var votosUsados = temas.reduce(function (total, tema) {
        return total + tema.get('cantidadVotosPropios');
      }, 0);
      return 3 - votosUsados;
    }),

  terminoDeVotar:
    Ember.computed('votosRestantes', function () {
      return this.get('votosRestantes') === 0;
    }),
  puedeCerrar:
    Ember.computed('terminoDeVotar', 'reunion.status', function () {
      return this.get('terminoDeVotar') && this.get('reunion.status') !== 'CON_MINUTA';
    }),

  actions:
    {
      verObligatorios(mostrar) {
        this.set('mostrarObligatorios', mostrar);
      },

      sumarVoto(tema) {
        this._siNoEstaCerrada(function () {
          if (this.get('votosRestantes')) {
            this._votarPorTema(tema);
          }
        });
      },
      seleccionarDuracion(duracion) {
        this.set('nuevoTema.duracion', duracion);
      },

      cerrarEditorDeTemaNuevo() {
        this._ocultarEditorDeTemaNuevo();
        this._cerrarModalTema();
      },

      cerrarEditorDeTema() {
        this._ocultarEditorDeTema();
        this._recargarReunion();
        this._cerrarModalTema();
      },

      cerrarEditorDeProponerPino() {
        this._ocultarEditorDeProponerPino();
        this._cerrarModalTema();
      },

      restarVoto(tema) {
        this._siNoEstaCerrada(function () {
          this._quitarVotoDeTema(tema);
        });
      },
      mostrarFormularioDeEdicion(tema) {
        this._siNoEstaCerrada(function () {
          this._traerDuraciones().then(() => {
            this.set('temaAEditar', Tema.create({}));
            this.set('temaAEditar.id', tema.id);
            this.set('temaAEditar.idDePropuestaOriginal', tema.idDePropuestaOriginal);
            this.set('temaAEditar.tipo', tema.tipo);
            this.set('temaAEditar.duracion', tema.duracion);
            this.set('temaAEditar.idDeAutor', tema.idDeAutor);
            this.set('temaAEditar.ultimoModificador', tema.ultimoModificador);
            this.set('temaAEditar.idDeReunion', tema.idDeReunion);
            this.set('temaAEditar.prioridad', tema.prioridad);
            this.set('temaAEditar.titulo', tema.titulo);
            this.set('temaAEditar.descripcion', tema.descripcion);
            this.set('temaAEditar.linkDePresentacion', tema.linkDePresentacion);
            this.set('temaAEditar.idsDeInteresados', tema.idsDeInteresados);
            this.set('temaAEditar.obligatoriedad', tema.obligatoriedad);
            this.set('obligatoriedadPasada', tema.obligatoriedad);
            this.set('esObligatorio', this.get('temaAEditar.esObligatorio'));
            this.set('mostrandoFormularioXTemaNuevo', false);
            this.set('mostrandoFormularioDeEdicion', true);
            this._mostrarModalTema();
          });
        });
      },
      mostrarFormulario() {
        this._siNoEstaCerrada(function () {
          this._traerDuraciones().then(() => {
            this.set('mostrandoFormularioDeEdicion', false);
            this.set('mostrandoFormularioXTemaNuevo', true);
            this.set('nuevoTema', Tema.create({
              idDeReunion: this._idDeReunion(),
              idDeAutor: this._idDeUsuarioActual(),
            }));
            this._mostrarModalTema();
          });
        });
      },
      mostrarFormularioDeReproponer(tema) {
        this._siNoEstaCerrada(function () {
          this._traerDuraciones().then(() => {
            this.set('mostrandoFormularioDeEdicion', false);
            this.set('mostrandoFormularioXTemaNuevo', true);
            this.set('nuevoTema', Tema.create({
              idDeReunion: this._idDeReunion(),
              idDeAutor: this._idDeUsuarioActual(),
              idDePropuestaOriginal: tema.esRePropuesta ? tema.idDePropuestaOriginal : tema.id,
              duracion: tema.duracion,
              titulo: tema.titulo,
              descripcion: tema.descripcion,
              obligatoriedad: tema.obligatoriedad,
              esObligatorio: tema.esObligatorio
            }));
          });
          this._mostrarModalTema();
        });
      },
      mostrarFormularioDeProponerPino() {
        this.set('mostrandoFormularioDeProponerPino', true);
        this._mostrarModalTema();
      },
      agregarTema() {
        this._guardarTemaYRecargar().then(() => {
          this._limpiarObligatoriedad();
          this._cerrarModalTema();
        });
      },

      updatearTemaConfirmado() {
        this.set('temaAEditar.idsDeInteresados', []);
        this._updatearTemaYRecargar().then(() => {
          this._cerrarModalTema();
        });
      },

      updatearTema() {
        var tema = this.get('temaAEditar');

        tema.set('obligatoriedad', this._obligatoriedad(this.get('esObligatorio')));

        if (this.get('temaAEditar.obligatoriedad') === 'OBLIGATORIO' && this.get('obligatoriedadPasada') === 'NO_OBLIGATORIO') {
          this.set('modalDeCambioDeObligatoriedadAbierto', true);
        } else {
          this._updatearTemaYRecargar().then(() => {
            this._cerrarModalTema();
          });
        }
      },

      proponerPino(pino) {
        this.reunionService().proponerPino(this.get('reunion'), pino).then((reunion) => {
          this._ocultarEditorDeProponerPino();
          this._actualizarreunionCon(reunion);
        });
      },

      pedirConfirmacionDeBorrado(temaABorrar) {
        this.set('mostrandoFormularioDeEdicion', false);
        this.set('mostrandoFormularioXTemaNuevo', false);
        this.set('temaABorrar', temaABorrar);
        this.set('mensajeDeConfirmacionDeBorrado', `Estás seguro de borrar el tema "${temaABorrar.titulo}"? Los votos seran devueltos`);
        this.set('modalDeBorradoAbierto', true);
      },

      borrarTemaElegido(temaABorrar) {
        delete this.temaABorrar; // Desreferenciamos el objeto
        this._quitarTema(temaABorrar);
        this._ocultarEditorDeTema();
      },

      editarFecha() {
        this._siNoEstaCerrada(function () {
          this.set('editandoFecha', true);
        });
      },

      pedirConfirmacionDeCierre() {
        this.set('modalDeCierreAbierto', true);
      },

      cerrarVotacion() {
        this._cerrarReunion();
      },

      reabrirVotacion() {
        this._reabrirReunion();
      },

      fueModificado(tema) {
        return tema.autor.login !== tema.ultimoModificador.login;
      }
    },

  _resaltarCuando(condicion) {
    if (condicion) {
      return "resaltar";
    } else {
      return "no-resaltar";
    }
  },

  _contarReunionesCon(obligatoriedad) {
    return this.get('reunion.temasPropuestos').filter(function (tema) {
      return tema.obligatoriedad === obligatoriedad
    }).length;
  },

  _limpiarObligatoriedad() {
    this.set('esObligatorio', false);
  },
  _ocultarEditorDeTema() {
    this.set('mostrandoFormularioDeEdicion', false);
  },
  _ocultarEditorDeTemaNuevo() {
    this.set('mostrandoFormularioXTemaNuevo', false);
  },
  _ocultarEditorDeProponerPino() {
    this.set('mostrandoFormularioDeProponerPino', false);
  },

  _traerDuraciones() {
    return this.duracionesService().getAll().then((duraciones) => {
      this.set('duraciones', duraciones);
    });
  },
  _guardarCambios() {
    var reunion = this.get('reunion');
    return this.reunionService().updateReunion(reunion)
      .then((reunionGuardada) => {
        this._actualizarreunionCon(reunionGuardada);
      });
  },

  _recargarReunion() {
    return this.reunionService().getReunion(this._idDeReunion()).then((reunion) => {
      this._actualizarreunionCon(reunion);
    });
  },

  _updatearTemaYRecargar: function () {
    let tema = this.get('temaAEditar');
    return this.temaService().updateTema(tema).then(() => {
      this.set('mostrandoFormularioDeEdicion', false);
      this._recargarReunion();
    });
  },
  _guardarTemaYRecargar: function () {
    var tema = this.get('nuevoTema');
    tema.obligatoriedad = this._obligatoriedad(this.get('esObligatorio'));
    return this.temaService().createTema(tema).then(() => {
      this.set('mostrandoFormularioXTemaNuevo', false);
      this._recargarReunion();
    });
  },
  _borrarTemaYRecargar(tema) {
    this.temaService().removeTema(tema).then(() => {
      this._recargarReunion();
    });
  },

  _idDeReunion() {
    return this.get('reunion.id');
  },

  _idDeUsuarioActual() {
    return this.get('usuarioActual.id');
  },

  _votarPorTema(tema) {
    this._agregarVoto(tema);
    this.temaService().votarTema(tema.id).then(() => this._recargarTemas(), () => {
      this._quitarVoto(tema);
      this.set('updatingVotos', false);
    });
  },

  _agregarVoto(tema) {
    tema.agregarInteresado(this._idDeUsuarioActual());
    this._actualizarVotantesDelTema(tema);
  },

  _quitarVoto(tema) {
    tema.quitarInteresado(this._idDeUsuarioActual());
    this._actualizarVotantesDelTema(tema);
  },

  _actualizarVotantesDelTema(tema) {
    this.set('updatingVotos', true);
    let temaPropuestos = this.get('reunion.temasPropuestos');
    let index = temaPropuestos.indexOf(tema);
    temaPropuestos[index] = tema;
    this.set('reunion.temaPropuestos', temaPropuestos);
  },

  _quitarVotoDeTema(tema) {
    this._quitarVoto(tema);
    this.temaService().quitarVotoTema(tema.id).then(() => this._recargarTemas(), () => {
        this._agregarVoto(tema);
        this.set('updatingVotos', false);
      }
    );
  },

  _recargarTemas() {
    this._recargarReunion().then(() =>
      this.set('updatingVotos', false)
    );
  },

  _usarInstanciasDeTemas(reunion, usuarioActual) {
    var temasPropuestos = reunion.get('temasPropuestos');
    for (var i = 0; i < temasPropuestos.length; i++) {
      var objetoEmber = temasPropuestos[i];
      objetoEmber.set('usuarioActual', usuarioActual);
      temasPropuestos[i] = Tema.create(objetoEmber);
    }
  },

  _filtrarTemasGeneradosPorTemasGenerales(reunion) {
    var temasFiltrados = reunion.get('temasPropuestos').filter(function (tema) {
      return !tema.get('esDeUnTemaGeneral');
    });
    reunion.set('temasPropuestos', temasFiltrados);
  },

  _cerrarReunion() {
    var reunion = this.get('reunion');
    this.reunionService().cerrarReunion(reunion)
      .then(() => {
        this.navigator().navigateToVerMinuta(reunion.id);
      });
  },

  _reabrirReunion() {
    var reunion = this.get('reunion');
    this.reunionService().reabrirReunion(reunion)
      .then((abierta) => {
        this._actualizarreunionCon(abierta);
      });
  },

  _actualizarreunionCon(reunion) {
    this._usarInstanciasDeTemas(reunion, this.get('usuarioActual'));
    this._filtrarTemasGeneradosPorTemasGenerales(reunion);
    this.set('model.reunion', reunion);
  },

  _siNoEstaCerrada(accion) {
    if (!this.get('estaCerrada')) {
      accion.call(this);
    }
  },

  _quitarTema(tema) {
    this._siNoEstaCerrada(function () {
      this._borrarTemaYRecargar(tema);
    });
  },

  _obligatoriedad(esObligatorio) {
    return esObligatorio ? "OBLIGATORIO" : "NO_OBLIGATORIO";
  },

  _mostrarModalTema() {
    this.set('visibilidadCardDeTema', true);
  },

  _cerrarModalTema() {
    this.set('visibilidadCardDeTema', false);
  }
});
