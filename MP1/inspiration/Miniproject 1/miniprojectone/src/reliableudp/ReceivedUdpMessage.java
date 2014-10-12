package reliableudp;

public class ReceivedUdpMessage {

	private String message;
	private String receivedFromHost;
	private int receivedFromPort;
	
	public String getMessage() { return message; }
	public String getReceivedFromHost() { return receivedFromHost; }
	public int getReceivedFromPort() { return receivedFromPort; }
	
	public ReceivedUdpMessage(String message, String receivedFromHost, int receivedFromPort) {
		this.message = message;
		this.receivedFromHost = receivedFromHost;
		this.receivedFromPort = receivedFromPort;
	}
	
	public ReceivedUdpMessage(String[] messageArray, String receivedFromHost, int receivedFromPort) {
		StringBuilder sb = new StringBuilder();
		for (String s : messageArray) {
			sb.append(s);
		}
		this.message = sb.toString();
		this.receivedFromHost = receivedFromHost;
		this.receivedFromPort = receivedFromPort;
	} 
	
}
