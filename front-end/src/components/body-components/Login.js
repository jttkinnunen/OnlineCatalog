import React from "react";
import { Col, Button, Form, FormGroup, FormFeedback, Label, Input, FormText } from 'reactstrap';


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

    render() {
        let login_result = "";

        if (this.props.login_state !== ""){
            login_result =
            this.props.login_state
        }

        return(
            <Form>
                <h5>Kirjaudu sisään</h5>
                <FormGroup row>
                    <Col sm={10}>
                        <Input type="email" name="email" id="exampleEmail" placeholder="Sähköposti" value = {this.state.user} onChange = {this.handleUserChange}/>
                    </Col>
                </FormGroup>
                <FormGroup row>
                    <Col sm={10}>
                        <Input type="password" name="password" id="examplePassword" placeholder="Salasana" value = {this.state.pass} onChange = {this.handlePassChange} />
                        <h6>{login_result}</h6>
                    </Col>
                </FormGroup>
                <FormGroup check row>
                    <Col sm={{ size: 10, offset: 1 }}>
                        <Button onClick={this.handleSubmit}>Kirjaudu</Button>
                    </Col>
                </FormGroup>
            </Form>
        );
    }
}

export default Login;