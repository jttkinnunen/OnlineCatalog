import React from "react";
import { Col, Button, Form, FormGroup, Label, Input, FormText } from 'reactstrap';


// TODO: Katso linkki alta, hyödyllinen
// TODO: muotoile, laita "this.props.login" argumenteiksi kenttien arvot placeholdereiden tilalle
// TODO: lisää "unohdin salasanani" linkki


// TODO: https://reactstrap.github.io/components/form/

class Login extends React.Component {
    render() {
        return(
            <Form>
                <h5>Kirjaudu sisään</h5>
                <FormGroup row>
                    <Label for="exampleEmail" sm={2}>Sähköposti</Label>
                    <Col sm={10}>
                        <Input type="email" name="email" id="exampleEmail" placeholder="Sähköposti" />
                    </Col>
                </FormGroup>
                <FormGroup row>
                    <Label for="examplePassword" sm={2}>Salasana</Label>
                    <Col sm={10}>
                        <Input type="password" name="password" id="examplePassword" placeholder="Salasana" />
                    </Col>
                </FormGroup>
                <FormGroup check row>
                    <Col sm={{ size: 10, offset: 2 }}>
                        <Button onClick={() => this.props.login("urpo","turpo")}>Kirjaudu</Button>
                    </Col>
                </FormGroup>
            </Form>
        );
    }
}

export default Login;