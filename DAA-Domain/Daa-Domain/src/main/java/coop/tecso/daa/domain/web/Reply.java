package coop.tecso.daa.domain.web;

/**
 * Response Envelope Standard OpenPhoto API response envelope
 * 
 * Every API returns a JSON response adhering to the following format.
 * 
 * { 
 *   message: (string), 
 *   code: (int), 
 *   result: (mixed) 
 * }
 * 
 * Message
 * 
 * The message is a string which describes the action taken. It's purely for
 * informational purposes and should never be used in your code or relied on.
 *
 * Code
 * 
 * The code is an integer representing the status of the API call. Typically the
 * code value should be 200 but anything between 200 and 299 indicates a
 * successful response. The API, for example, will return a 202 response indicating
 * that the resource has been created.
 * 
 * Below are some common codes:
 * 
 * 200, The API call was successful 
 * 202, Resource was created successfully 
 * 403, Authentication failed when trying to complete the API call 
 * 404, The requested endpoint could not be found 
 * 500, An unknown error occured and hopefully the message has more information
 * 
 * Result
 * 
 * The result can be any simple or complex value. Consult the endpoint you're
 * using for information on what the result will be.
 **/
public class Reply<T> {

	public Reply(Integer code, String message, T result) {
		this.code = code;
		this.message = message;
		this.result = result;
	}

	public final int code;
	public final String message;
	public final T result;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("Entidad --> {%s} ", getClass().getSimpleName()));
		builder.append(String.format("code: {%s}, ", code));
		builder.append(String.format("message: {%s}, ", message));
		builder.append(String.format("result: {%s}, ", result));
		builder.append("\n");
		return builder.toString();
	}
}