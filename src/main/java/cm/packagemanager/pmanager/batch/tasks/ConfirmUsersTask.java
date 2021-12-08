package cm.packagemanager.pmanager.batch.tasks;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

public class ConfirmUsersTask extends CommonTask {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("ConfirmUsers start..");

        // ... your code

        System.out.println("ConfirmUsers done..");
        return RepeatStatus.FINISHED;
    }
}
