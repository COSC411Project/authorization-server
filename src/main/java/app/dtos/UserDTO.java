package app.dtos;

public class UserDTO {

	private String email;
	private String phone;
	private String city;
	private String state;

	public UserDTO(String email, String phone, String city, String state) {
		super();
		this.email = email;
		this.phone = phone;
		this.city = city;
		this.state = state;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
}
