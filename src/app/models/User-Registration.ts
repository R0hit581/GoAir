export class UserRegistration {
  username: string;
  emailId: string;
  password: string;
  phone: string;

  constructor(
    username: string,
    emailId: string,
    password: string,
    phone: string
  ) {
    this.username = username;
    this.emailId = emailId;
    this.password = password;
    this.phone = phone;
  }
}
