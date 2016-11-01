const React = require('react');
const Util = require('./Util');
const $ = require("jquery");
const TITLE_HEIGHT = 100;
const CONTAINER_WIDTH = 500;
const CONTAINER_HEIGHT = 500;
const screens = {
  CONTESTS: 0,
  LOGIN: 1,
  SIGNUP: 2,
  SUBMITTING: 3,
  SUCCESS: 4
}

export class SetupScreen extends React.Component{
  constructor(props) {
    super(props);

    this.state = {
      error: false,
      index: props.startingScreen,
      contestSelectedIndex: -1,
      contestSelected: {}
    }
  }

  getUserInfo(token, callback) {
    const accountInfo = $.ajax({
      contentType: 'application/json',
      url: 'api/user',
      type: 'GET',
      beforeSend: (request) => {
        request.setRequestHeader('Authorization', token);
      }
    });

    $.when(accountInfo).done((response) => {
      console.log('account info:', response);
      this.props.updateSession('account', response.user);
      callback();
    });
  }

  submitLoginInfo(event) {
    event.preventDefault();
    const email = document.getElementById('email-login-field').value;
    const password = document.getElementById('password-login-field').value;
    const fields = document.getElementById('registration-fields');

    this.clearErrors(['registration-fields']);
    const payload = {
      "email": email,
      "password": password
    }
    const login = $.ajax({
      contentType: 'application/json',
      url: 'api/auth/login',
      type: 'POST',
      data: JSON.stringify(payload)
    });

    $.when(login).done((response) => {
      console.log(response);
      if (response.success) {
        console.log('logged in!');
        this.props.updateSession('auth-token', response.token);
        this.getUserInfo(response.token, () => {
          Util.fadeElement('setup-screen', this.props.closeSetupScreen);
        });
      } else {
        fields.classList.add('error');
      }
    });
  }

  transitionScreen(screenIndex) {
    this.setState({
      index: screenIndex
    });
  }

  clearErrors(fields) {
    fields.forEach((field) => {
      document.getElementById(field).classList.remove('error');
    });
  }

  cancelSignup() {
    const form = document.getElementById('signup-form');
    form.reset();

    const fields = [
      'firstname-signup',
      'lastname-signup',
      'school-signup',
      'email-signup',
      'gradyear-signup',
      'password-signup',
      'password-confirmation-signup'
    ];
    this.clearErrors(fields);

    this.setState({
      index: screens.LOGIN
    });
  }

  submitSignup(event) {
    event.preventDefault();

    const firstNameField = document.getElementById('firstname-signup-field');
    const lastNameField = document.getElementById('lastname-signup-field');
    const schoolField = document.getElementById('school-signup-field');
    const emailField = document.getElementById('email-signup-field');
    const gradYearField = document.getElementById('gradyear-signup-field');
    const passwordField = document.getElementById('password-signup-field');
    const confirmPasswordField =
      document.getElementById('password-confirmation-signup-field');

    const firstName = firstNameField.value;
    const lastName = lastNameField.value;
    const school = schoolField.value;
    const email = emailField.value;
    const gradYear = gradYearField.value;
    const password = passwordField.value;
    const confirmPassword = confirmPasswordField.value;

    const fields = [
      'firstname-signup',
      'lastname-signup',
      'school-signup',
      'email-signup',
      'gradyear-signup',
      'password-signup',
      'password-confirmation-signup'
    ];

    const nonBlankValues = {
      'firstname-signup': firstName,
      'lastname-signup': lastName,
      'school-signup': school,
      'email-signup': email,
      'gradyear-signup': gradYear,
    }

    let hasErrors = false;
    this.clearErrors(fields);

    for(let key in nonBlankValues) {
      if (nonBlankValues[key].length == 0) {
        if (key == 'email-signup') {
          document.getElementById('email-signup-error').innerText =
            'Cannot be blank';
        }
        document.getElementById(key).classList.add('error');
        hasErrors = true;
      }
    }

    if (password.length <= 6) {
      document.getElementById('password-signup').classList.add('error');
      hasErrors = true;
    }

    if (password != confirmPassword) {
      document.getElementById(
        'password-confirmation-signup'
      ).classList.add('error');
      hasErrors = true;
    }

    if (hasErrors) {
      return;
    }

    const payload = {
      "email": email,
      "name": firstName + " " + lastName,
      "password": password,
      "school": school,
      "classInSchool": gradYear,
    }

    const emailPayload = {
      "email": email
    }

    const emailCheck = $.ajax({
      contentType: 'application/json',
      url: 'api/quickfetch/email',
      type: 'POST',
      data: JSON.stringify(emailPayload)
    });

    $.when(emailCheck).done((response) => {
      // console.log(response);
      if (!response.exists) {
        console.log('email does not exist already');
        const registration = $.ajax({
          contentType: 'application/json',
          url: 'api/auth/register',
          type: 'POST',
          data: JSON.stringify(payload)
        });

        this.setState({
          index: screens.SUBMITTING
        });

        $.when(registration).done((response) => {
          console.log(response);
          hasErrors = !response.success;
          if (response.success) {
            console.log('success!');
            this.props.updateSession('auth-token', response.token);
            this.getUserInfo(response.token, () => {
              this.setState({
                index: screens.SUCCESS
              });
            });
          } else {
            console.log('unsuccess!');
            this.setState({
              index: screens.SIGNUP
            }, () => {
              const name = payload.name.split(' ');
              document.getElementById('firstname-signup-field').value =
                name[0];
              document.getElementById('lastname-signup-field').value =
                name[1];
              document.getElementById('school-signup-field').value =
                payload.school;
              document.getElementById('gradyear-signup-field').value =
                payload.classInSchool;
              document.getElementById('email-signup-field').value =
                payload.email;
              document.getElementById('password-signup-field').value =
                payload.password;
              document.getElementById(
                'password-confirmation-signup-field'
              ).value = payload.password;

              let error = document.getElementById('email-signup-error');
              error.innerText = 'Invalid email address';
              document.getElementById('email-signup').classList.add('error');
            });
          }
        });
      } else {
        let error = document.getElementById('email-signup-error');
        error.innerText = 'Email is already in use';
        document.getElementById('email-signup').classList.add('error');
      }
    });
  }

  finishSignup() {
    Util.fadeElement('setup-screen', () => {
      this.props.closeSetupScreen();
    });
  }

  // after contest has been selected
  showLogin() {
    if (this.state.contestSelectedIndex == -1) {
      return;
    }
    this.props.updateSession('contest', this.state.contestSelected);
    this.transitionScreen(screens.LOGIN);
  }

  selectContest(index, contest) {
    const contestSelectedIndex = index === this.state.contestSelectedIndex ? -1 : index;
    this.setState({
      contestSelectedIndex: contestSelectedIndex,
      contestSelected: contest
    });
  }

  renderContests() {
    const contests = $.ajax({
      contentType: 'application/json',
      url: 'api/contests',
      type: 'GET'
    });

    let contestList = [];
    $.when(contests).done((response) => {
      contestList = response.contests;
    });

    // TODO: use actual contests API request to populate
    contestList.push({
      "contestId": "1",
      "contestName": "Duke Programming Contest 2016",
      "startTime": "string",
      "endTime": "string"
    });

    let contestElements = [];
    contestList.forEach((contest, index) => {
      let contestElement = (
        <div
          key={"contest-" + contest.contestId}
          className={
            this.state.contestSelectedIndex == index
            ? "contest-selector-option selected"
            : "contest-selector-option"
          }
          onClick={this.selectContest.bind(this, index, contestList[index])}>
          <div className="contest-option-name">
            {contest.contestName}
          </div>
          <div className="contest-selection-checkmark">
            <img
              className="contest-selection-checkmark-image"
              src="assets/white-checkmark.png"/>
          </div>
        </div>
      );
      contestElements.push(contestElement);
    });

    return contestElements;
  }

  displayForm() {
    switch (this.state.index) {
      case screens.CONTESTS:
        return (
          <div className="contest-selector" style={{
            left: (CONTAINER_WIDTH - 400)/2 + 'px',
            top: (CONTAINER_HEIGHT - 350)/2 + 'px'
          }}>
            <div className="contest-selector-header unselectable">
              Select A Competition
            </div>
            <div className="open-contests unselectable">
              {this.renderContests()}
            </div>
            <div
              className={
                this.state.contestSelectedIndex == -1
                ? "link-button next disabled"
                : "link-button next"
              }
              onClick={this.showLogin.bind(this)}>
              Next
            </div>
          </div>
        );
      case screens.LOGIN:
        return (
          <div style={{
            width: '300px',
            position: 'absolute',
            left: (CONTAINER_WIDTH - 300)/2 + 'px',
            top: '20px'
          }}>
            <div className="registration-form-login unselectable">
              Registered User?
            </div>
            <div id="registration-fields" className="registration-fields">
              <form onSubmit={(e) => this.submitLoginInfo(e)}>
                <input
                  type="text"
                  id="email-login-field"
                  className="text-field"
                  placeholder="Email Address"/>
                <input
                  type="password"
                  id="password-login-field"
                  className="text-field"
                  placeholder="Password"
                  autoComplete="new-password"/>
                <input type="submit" style={{display: 'none'}}/>
              </form>
              <div className="error-message">
                Email and password combination does not exist
              </div>
            </div>
            <div
              className="link-button login"
              onClick={this.submitLoginInfo.bind(this)}>
              Login
            </div>
            <div style={{
              width: '200px',
              marginLeft: '50px',
              marginTop: '60px'
            }}>
              <hr className="registration-divider"/>
              <div style={{
                backgroundColor: 'white',
                textAlign: 'center',
                width: '50px',
                position: 'relative',
                top: '-20px',
                left: '75px'
              }}>
                OR
              </div>
            </div>
            <div
              className="link-button register registration-form-sign-up-link"
              onClick={this.transitionScreen.bind(this, screens.SIGNUP)}>
              <i className="fa fa-pencil-square-o" aria-hidden="true"></i>
              &nbsp; Sign Up
            </div>
          </div>
        );
      case screens.SIGNUP:
        return (
          <div className="signup-form" style={{height: CONTAINER_HEIGHT + 'px'}}>
            <div className="signup-form-header">
              Sign Up
            </div>
            <div className="signup-fields">
              <form id="signup-form" onSubmit={this.submitSignup.bind(this)}>
                <div id="firstname-signup">
                  <input
                    type="text"
                    id="firstname-signup-field"
                    className="text-field"
                    placeholder="First Name"/>
                  <div className="error-message">
                    Cannot be blank
                  </div>
                </div>
                <div id="lastname-signup">
                  <input
                    type="text"
                    id="lastname-signup-field"
                    className="text-field"
                    placeholder="Last Name"/>
                  <div className="lastname error-message">
                    Cannot be blank
                  </div>
                </div>
                <div id="school-signup">
                  <input
                    type="text"
                    id="school-signup-field"
                    className="text-field"
                    placeholder="School"/>
                  <div className="school error-message">
                    Cannot be blank
                  </div>
                </div>
                <div id="gradyear-signup">
                  <input
                    type="text"
                    id="gradyear-signup-field"
                    className="text-field"
                    placeholder="Graduation Year"/>
                  <div className="gradyear error-message">
                    Cannot be blank
                  </div>
                </div>
                <div id="email-signup">
                  <input
                    type="text"
                    id="email-signup-field"
                    className="text-field"
                    placeholder="Email Address"/>
                  <div id="email-signup-error" className="email error-message">
                    Cannot be blank
                  </div>
                </div>
                <div id="password-signup">
                  <input
                    type="password"
                    id="password-signup-field"
                    className="text-field"
                    placeholder="Password"
                    autoComplete="new-password"/>
                  <div className="password error-message">
                    Must be longer than 6 characters
                  </div>
                </div>
                <div id="password-confirmation-signup">
                  <input
                    type="password"
                    id="password-confirmation-signup-field"
                    className="text-field"
                    placeholder="Re-enter Password"
                    autoComplete="new-password"/>
                  <div className="confirm-password error-message">
                    Must match password above
                  </div>
                </div>
                <input type="submit" style={{display: 'none'}}/>
              </form>
            </div>
            <div className="signup-buttons">
              <div
                className="link-button signup-submit"
                onClick={this.submitSignup.bind(this)}>
                Submit
              </div>
              <div
                className="link-button signup-cancel"
                onClick={this.cancelSignup.bind(this)}>
                Cancel
              </div>
            </div>
          </div>
        );
      case screens.SUBMITTING:
        return (
          <div className="signup-submitting" style={{
            left: (CONTAINER_WIDTH-400)/2 + 'px',
            top: (CONTAINER_HEIGHT-300)/2 + 'px'
          }}>
            <div className="signup-submitting-message">
              Setting up your account...
            </div>
            <div>
              <img
                className="signup-submitting-spinner"
                src="assets/submitting.gif"/>
            </div>
          </div>
        );
      case screens.SUCCESS:
        return (
          <div className="signup-success" style={{
            left: (CONTAINER_WIDTH-400)/2 + 'px',
            top: (CONTAINER_HEIGHT-350)/2 + 'px'
          }}>
            <div className="signup-success-message">
              You&lsquo;ve successfully signed up!
            </div>
            <div className="signup-checkmark-container">
              <img
                className="signup-checkmark"
                src="assets/checkmark.png"/>
            </div>
            <div
              className="link-button signup-success-done"
              onClick={this.finishSignup.bind(this)}>
              Done
            </div>
          </div>
        );
    }
  }

  render() {
    const width = this.props.dimensions.width;
    const height = this.props.dimensions.height;

    return (
      <div className="registration-tab">
        <div
          id="setup-screen-container"
          className="setup-screen-container"
          style={{
            left: (width - CONTAINER_WIDTH)/2 + 'px',
            top: (height - CONTAINER_HEIGHT)/2 + 'px',
            width: CONTAINER_WIDTH + 'px',
            height: CONTAINER_HEIGHT + 'px',
          }}>
          {this.displayForm()}
        </div>
      </div>
    );
  }
}

module.exports = SetupScreen;
