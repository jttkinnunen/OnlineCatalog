import React from "react";
import { Col, Button, Jumbotron, Form, FormGroup, FormFeedback, Label, Input, FormText } from 'reactstrap';
import '../../css/Login.css';


// TODO: Katso linkki alta, hyödyllinen
// TODO: muotoile, laita "this.props.login" argumenteiksi kenttien arvot placeholdereiden tilalle
// TODO: lisää "unohdin salasanani" linkki


// TODO: https://reactstrap.github.io/components/form/

class Login extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            user: '',
            pass: '',
        };
        this.handleUserChange = this.handleUserChange.bind(this);
        this.handlePassChange = this.handlePassChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleKeyPress = this.handleKeyPress.bind(this);
        this.handleForgot = this.handleForgot.bind(this);

    }
    handleKeyPress = (event) => {
        if(event.key === 'Enter'){
            event.preventDefault();
            event.stopPropagation();
            this.handleSubmit();
        }
    }

    handleUserChange(event) {
        this.setState({
            user: event.target.value,
        })
    }

    handlePassChange(event) {
        this.setState({
            pass: event.target.value,
        })
    }

    handleSubmit() {
        this.props.login(this.state.user, this.state.pass);
    }

    handleForgot() {
        this.props.setView("forgot-pass");
    }

    render() {
        let login_result = "";

        if (this.props.login_state !== ""){
            login_result =
            this.props.login_state
        }

        return(
             <Jumbotron>
                <Form>
                    <h5 className = "white-h5">Kirjaudu sisään</h5>
                    <h6 className = "white-h6">(Käytä sähköpostia erkki.esimerkki@fcg.fi ja salasanaa 1234)</h6>

                    <FormGroup>
                        <span>
                            {/*<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-mail"><path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path><polyline points="22,6 12,13 2,6"></polyline></svg>*/}
                            <Input type="email" name="email" id="exampleEmail" placeholder="Sähköposti" value = {this.state.user} onChange = {this.handleUserChange} />
                        </span>
                     </FormGroup>
                    <FormGroup>
                        <span>

                            {/*<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                                fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                                stroke-linejoin="round" className="feather feather-lock"><rect x="3" y="11" width="18"
                                                                                               height="11" rx="2"
                                                                                               ry="2"></rect><path
                               d="M7 11V7a5 5 0 0 1 10 0v4"></path></svg>*/}

                            <Input type="password" name="password" id="examplePassword" placeholder="Salasana" value = {this.state.pass} onChange = {this.handlePassChange} onKeyDown={this.handleKeyPress}  tabIndex="0" />
                                <br/>

                        </span>
                    </FormGroup>
                    <h6 className = "standard-text-color">{login_result}</h6>
                <FormGroup>

                        <Button className="btn btn-success btn-wide" onClick={this.handleSubmit}>Kirjaudu</Button>

                </FormGroup>
                    <br/>
                    <FormGroup>

                        <a href="#" onClick={this.handleForgot}>Unohtuiko salasana?</a>

                    </FormGroup>
            </Form>
            </Jumbotron>
        );
    }
}

export default Login;