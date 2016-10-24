const React = require('react');
const $ = require("jquery");
const TITLE_HEIGHT = 100;
const CONTAINER_WIDTH = 500;
const CONTAINER_HEIGHT = 450;
const screens = {
  CONTESTS: 0,
  LOGIN: 1,
  SIGNUP: 2,
  SUCCESS: 3
}

export class SetupScreen extends React.Component{
  constructor(props) {
    super(props);

    this.state = {
      error: false,
      index: screens.CONTESTS,
      contestSelected: -1
    }
  }

  submitLoginInfo(event) {
    event.preventDefault();
    const email = document.getElementById('email-login-field').value;
    const password = document.getElementById('password-login-field').value;
    console.log('email:', email, ',', 'password:', password);
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
    });
  }

  transitionScreen(screenIndex) {
    this.setState({
      index: screenIndex
    });
  }

  finishSignup(shouldSubmit, event) {
    event.preventDefault();

    if (shouldSubmit) {
      const firstName = document.getElementById('firstname-signup-field').value;
      const lastName = document.getElementById('lastname-signup-field').value;
      const school = document.getElementById('school-signup-field').value;
      const email = document.getElementById('email-signup-field').value;
      const gradYear = document.getElementById('gradyear-signup-field').value;
      const password = document.getElementById('password-signup-field').value;
      const confirmPassword =
        document.getElementById('password-confirmation-signup-field').value;
      if (password != confirmPassword) {
        console.log('passwords don\'t match');
      }
      const payload = {
        "email": email,
        "name": firstName + " " + lastName,
        "password": password,
        "school": school,
        "classInSchool": gradYear,
      }

      const registration = $.ajax({
        contentType: 'application/json',
        url: 'api/auth/register',
        type: 'POST',
        data: JSON.stringify(payload)
      });

      $.when(registration).done((response) => {
        console.log(response);
      });
    }

    const destination = shouldSubmit ? screens.SUCCESS : screens.LOGIN;
    this.setState({
      index: destination
    });
  }

  showLogin() {
    if (this.state.contestSelected == -1) {
      return;
    }
    this.transitionScreen(screens.LOGIN);
  }

  selectContest(index) {
    const contestSelected = index === this.state.contestSelected ? -1 : index;
    this.setState({
      contestSelected: contestSelected
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
            this.state.contestSelected == index
            ? "contest-selector-option selected"
            : "contest-selector-option"
          }
          onClick={this.selectContest.bind(this, index)}>
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
                this.state.contestSelected == -1
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
            <div className="registration-fields">
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
            </div>
            <div
              className="link-button login"
              onClick={this.submitLoginInfo.bind(this)}>
              Login
            </div>
            <div style={{
              width: '200px',
              marginLeft: '50px',
              marginTop: '40px'
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
              <form onSubmit={this.finishSignup.bind(this, true)}>
                <input
                  type="text"
                  id="firstname-signup-field"
                  className="text-field"
                  placeholder="First Name"/>
                <input
                  type="text"
                  id="lastname-signup-field"
                  className="text-field"
                  placeholder="Last Name"/>
                <input
                  type="text"
                  id="school-signup-field"
                  className="text-field"
                  placeholder="School"/>
                <input
                  type="text"
                  id="gradyear-signup-field"
                  className="text-field"
                  placeholder="Graduation Year"/>
                <input
                  type="text"
                  id="email-signup-field"
                  className="text-field"
                  placeholder="Email Address"/>
                <input
                  type="password"
                  id="password-signup-field"
                  className="text-field"
                  placeholder="Password"
                  autoComplete="new-password"/>
                <input
                  type="password"
                  id="password-confirmation-signup-field"
                  className="text-field"
                  placeholder="Re-enter Password"
                  autoComplete="new-password"/>
                <input type="submit" style={{display: 'none'}}/>
              </form>
            </div>
            <div className="signup-buttons">
              <div
                className="link-button signup-submit"
                onClick={this.finishSignup.bind(this, true)}>
                Submit
              </div>
              <div
                className="link-button signup-cancel"
                onClick={this.finishSignup.bind(this, false)}>
                Cancel
              </div>
            </div>
          </div>
        );
      case screens.SUCCESS:
        return(
          <div className="signup-success" style={{
            left: (CONTAINER_WIDTH-400)/2 + 'px',
            top: (CONTAINER_HEIGHT-300)/2 + 'px'
          }}>
            <div className="signup-success-message">
              You&lsquo;ve successfully signed up!
            </div>
            <div className="signup-checkmark-container">
              <img
                className="signup-checkmark"
                src="assets/checkmark.png"
                width="150"
                height="150"/>
            </div>
            <div className="link-button signup-success-done">
              Ok
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
