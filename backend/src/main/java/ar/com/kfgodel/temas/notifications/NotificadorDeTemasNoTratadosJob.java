package ar.com.kfgodel.temas.notifications;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.time.Clock;

public class NotificadorDeTemasNoTratadosJob implements Job {
    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        DependencyInjector injector = (DependencyInjector) dataMap.get("injector");
        NotificadorDeTemasNoTratados notificador = NotificadorDeTemasNoTratados
                .create(injector, Clock.systemDefaultZone(), MailerConfiguration.getMailer());
        notificador.notificar();
    }
}
