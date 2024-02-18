package com.example.demo.handler;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.example.demo.DemoApplication;

public class Handler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
	private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

	static {
		try {
			handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(DemoApplication.class);
		} catch (ContainerInitializationException ex) {
			throw new RuntimeException("Unable to load spring boot Demo application", ex);
		}
	}

	@Override
	public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
		return handler.proxy(input, context);
	}

}
