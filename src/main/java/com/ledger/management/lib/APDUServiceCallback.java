package com.ledger.management.lib;

import java.util.concurrent.BlockingQueue;

import okhttp3.WebSocketListener;
import okhttp3.WebSocket;
import okhttp3.Response;
import okio.ByteString;

/**
 * \brief Internal Websocket callback used by an APDUSevice
 */
public class APDUServiceCallback extends WebSocketListener {

	private BlockingQueue<SocketEvent> queue;

	enum SocketEventType {
		SOCKET_READY,
		SOCKET_MESSAGE,
		SOCKET_ERROR
	};

	class SocketEvent {
		private SocketEventType eventType;
		private String data;
		private Throwable exception;

		public SocketEvent() {
			this.eventType = SocketEventType.SOCKET_READY;
		}

		public SocketEvent(String data) {
			this.eventType = SocketEventType.SOCKET_MESSAGE;
			this.data = data;
		}

		public SocketEvent(Throwable exception) {
			this.eventType = SocketEventType.SOCKET_ERROR;
			this.exception = exception;
		}

		public SocketEventType getEventType() {
			return eventType;
		}
		public String getData() {
			return data;
		}
		public Throwable getException() {
			return exception;
		}

		public String toString() {
			switch(eventType) {
				case SOCKET_READY:
					return "SOCKET_READY";
				case SOCKET_MESSAGE:
					return "SOCKET_MESSAGE " + data;
				case SOCKET_ERROR:
					return "SOCKET_ERROR " + exception;
			}
			return "Unsupported event";
		}
	}

	public APDUServiceCallback(BlockingQueue<SocketEvent> queue) {
		this.queue = queue;
	}

	@Override
	public void onClosed(WebSocket webSocket, int code, String reason) {		
		queue.add(new SocketEvent(new RuntimeException("Socket closed")));
	}

	@Override
	public void onClosing(WebSocket webSocket, int code, String reason) {		
		queue.add(new SocketEvent(new RuntimeException("Socket closing")));
	}

	@Override
	public void onFailure(WebSocket webSocket, Throwable t, Response response) {		
		queue.add(new SocketEvent(t));
	}

	@Override
	public void onMessage(WebSocket webSocket, okio.ByteString bytes) {		
		queue.add(new SocketEvent(new RuntimeException("Unexpected message")));
	}

	@Override
	public void onMessage(WebSocket webSocket, String text) {		
		queue.add(new SocketEvent(text));
	}

	@Override
	public void onOpen(WebSocket webSocket, Response response) {		
		queue.add(new SocketEvent());
	}
}
