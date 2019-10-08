import Ember from "ember";

const tiposDeTema = {
  PROPONER_PINOS: "proponerPinos",
  CON_DESCRIPCION: "conDescripcion",
  REPASAR_ACTION_ITEMS: "repasarActionItems"
};

export default Ember.Object.extend({

  cantidadVotosTotales: Ember.computed('idsDeInteresados.[]', function () {
    return this.get('idsDeInteresados.length');
  }),

  colorVotos: Ember.computed('cantidadVotosPropios', function () {
    return 'white';
  }),

  colorDuracion: Ember.computed('tema.color', function () {
    if (this.duracion === "LARGO") {
      return 'red-text';
    }
    if (this.duracion === "MEDIO") {
      return 'orange-text';
    }
    if (this.duracion === "CORTO") {
      return 'green-text';
    }
  }),

  tieneVotos: Ember.computed('tieneVotos', 'cantidadVotosPropios', function () {
    return this.get('cantidadVotosPropios') > 0;
  }),

  noTieneVotos: Ember.computed('noTieneVotos', 'cantidadVotosPropios', function () {
    return this.get('cantidadVotosPropios') === 0;
  }),

  cantidadVotosPropios: Ember.computed('idsDeInteresados.[]', 'usuarioActual', function () {
    var idDeUsuarioActual = this.get('usuarioActual.id');
    var votosDelUsuario = this.get('idsDeInteresados').filter(function (idDeInteresado) {
      return idDeInteresado === idDeUsuarioActual;
    });
    return votosDelUsuario.length;
  }),

  puedeSerBorrado: Ember.computed('idDeAutor', 'usuarioActual.id', function () {
    var idDeAutor = this.get('idDeAutor');
    var idDelUsuarioActual = this.get('usuarioActual.id');
    return idDeAutor === idDelUsuarioActual;
  }),

  esObligatorio: Ember.computed('obligatoriedad', function () {
    let obligatoriedad = this.get('obligatoriedad');
    return obligatoriedad === "OBLIGATORIO" || obligatoriedad === "OBLIGATORIO_GENERAL";
  }),

  esRePropuesta: Ember.computed('esRePropuesta', function(){
    return this.get('esRePropuesta');
  }),

  antiguedadDePropuesta: Ember.computed('fechaDePropuestaOriginal', function () {
    return moment(this.get('fechaDePropuestaOriginal')).locale('es').fromNow();
  }),

  agregarInteresado(idDeInteresado) {
    this.get('idsDeInteresados').pushObject(idDeInteresado);
  },

  quitarInteresado(idDeInteresado) {
    var interesados = this.get('idsDeInteresados');
    var indice = interesados.indexOf(idDeInteresado);
    if (indice >= 0) {
      interesados.removeAt(indice);
    }
  },

  esTemaConDescripcion: Ember.computed('tipo', function () {
    return this.get('tipo') === tiposDeTema.CON_DESCRIPCION;
  }),
  esTemaParaProponerPinos: Ember.computed('tipo', function () {
    return this.get('tipo') === tiposDeTema.PROPONER_PINOS;
  }),
  esTemaParaRepasarActionItems: Ember.computed('tipo', function () {
    return this.get('tipo') === tiposDeTema.REPASAR_ACTION_ITEMS;
  })
});
