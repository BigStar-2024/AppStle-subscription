package com.et.stepfunction;

import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.model.SendTaskHeartbeatRequest;

import java.util.TimerTask;

public class HeartbeatTask extends TimerTask {

    private final AWSStepFunctions awsStepFunctions;
    private final String taskToken;

    public HeartbeatTask(String taskToken, AWSStepFunctions awsStepFunctions) {
        this.awsStepFunctions = awsStepFunctions;
        this.taskToken = taskToken;
    }

    @Override
    public void run() {
        awsStepFunctions.sendTaskHeartbeat(new SendTaskHeartbeatRequest().withTaskToken(taskToken));
    }
}
