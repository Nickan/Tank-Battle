package com.nickan.framework.finitestatemachine.messagingsystem;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.nickan.framework.finitestatemachine.BaseEntity;
import com.nickan.framework.finitestatemachine.EntityManager;
import com.nickan.framework.finitestatemachine.messagingsystem.Message.MessageType;

/**
 * Handles the messages being sent, doesn't need to have an instance, just update it to handle the message
 * that will be sent via dispatchMessage(). All the communication and interaction should be sent here.
 * @author Nickan
 *
 */

public class MessageDispatcher {
	
	private static Array<Message> messages = new Array<Message>();
	private static Pool<Message> poolMessages;
	
	// Just to make sure that the Constructor will be called to initialize some variables
	@SuppressWarnings("unused")
	private static final MessageDispatcher instance = new MessageDispatcher();
	
	private MessageDispatcher() {
		// Why when I call the initialize() using the private instance, it throws "java.lang.ExceptionInInitializerError"?
		initialize();
		clear();
	}

	private void initialize() {
		// Limit the creation of the message
		poolMessages = new Pool<Message>(50) {

			@Override
			protected Message newObject() {
				return new Message();
			}
			
		};
	}
	
	public static void clear() {
		poolMessages.clear();
		messages.clear();
	}

	public static void update(float delta) {
		
		for (int index = 0; index < messages.size;++index) {
			Message message = messages.get(index);
			message.dispatchTime -= delta;
			
			if (message.dispatchTime <= 0) {
				if (dispatchMessage(message)) {
					
					// Delete the message from the messages
					messages.removeIndex(index);
					poolMessages.free(message);
					--index;
				}
			}
		}

	}
	
	private static boolean dispatchMessage(Message message) {
		BaseEntity entity = EntityManager.getEntity(message.receiverId);
		
		return entity.handleMessage(message);
	}
	
	public static void sendMessage(int senderId, int receiverId, float dispatchTime, 
			MessageType type, Object extraInfo) {
		Message message = poolMessages.obtain();
		
		// Initialize the message
		message.senderId = senderId;
		message.receiverId = receiverId;
		message.dispatchTime = dispatchTime;
		message.type = type;
		message.extraInfo = extraInfo;
		
		messages.add(message);
	}

	
}
