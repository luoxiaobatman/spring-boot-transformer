package me.luoxiao.transformer.message.agnostic;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;


public interface MessageSender {

	ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor() {
		{
			setThreadNamePrefix("async-message-send-");
			setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
			initialize();
		}
	};

	void sendMessage(String topic, BaseMessage<?> message);
	
	default <T> void sendMessage(String topic, String msg,T data) {
		sendMessage(topic, new BaseMessage<T>(msg,data));
	}
	
	default <T> void sendMessage(String topic, String msg) {
		sendMessage(topic, new BaseMessage<T>(msg));
	}

	default <T> void sendMessage(String topic, T data) {
		sendMessage(topic, new BaseMessage<T>(data));
	}

	default <T> void sendFailMessage(String topic, T data) {
		sendMessage(topic, new BaseMessage<T>(data, MessageResult.FAILED));
	}

	default <T> void sendMessageAfterTransactionCommit(String topic, BaseMessage<?> message) {
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCommit() {
					sendMessage(topic, message);
				}
			});
		} else {
			sendMessage(topic, message);
		}
	}

	default <T> Future<?> asyncSendMessage(final String topic, final BaseMessage<?> message) {
		return executor.submit(new Runnable() {
			@Override
			public void run() {
				sendMessage(topic, message);
			}
		});
	}

	default <T> Future<?> asyncSendMessage(String topic, T data) {
		return asyncSendMessage(topic, new BaseMessage<T>(data));
	}
	

	default <T> Future<?> asyncSendMessage(String topic, String msg,T data) {
		return asyncSendMessage(topic, new BaseMessage<T>(msg,data));
	}
	
	default <T> Future<?> asyncSendMessage(String topic, String msg) {
		return asyncSendMessage(topic, new BaseMessage<T>(msg));
	}
	
	void sendNoPersistMessage(String topic, BaseMessage<?> message);
}
