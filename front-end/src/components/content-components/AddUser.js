import React from "react";
import { Form, Input, FormGroup, Label, Button} from "reactstrap";

class AddUser extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            first_name: '',
            last_name: '',
            email: '',
        };
        this.handleFirstNameChange = this.handleFirstNameChange.bind(this);
        this.handleLastNameChange = this.handleLastNameChange.bind(this);
        this.handleEmailChange = this.handleEmailChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleFirstNameChange(event) {
        this.setState({
            user: event.target.value,
        })
    }

    handleLastNameChange(event) {
        this.setState({
            pass: event.target.value,
        })
    }

    handleEmailChange(event) {
        this.setState({
            pass: event.target.value,
        })
    }

    handleSubmit() {
        this.props.login(this.state.user, this.state.pass);
    }

    render() {
        return(
            <Form>
                <h5 className = "white-h5">Luo uusi käyttäjä</h5>

                <FormGroup>
                    <span>
                        <Input type="email" name="email" id="Email" placeholder="Sähköposti" value = {this.state.email} onChange = {this.handleEmailChange}/>
                    </span>
                </FormGroup>
                <FormGroup>
                    <span>
                        <Input type="text" name="firstname" id="FirstName" placeholder="Etunimi" value = {this.state.first_name} onChange = {this.handleFirstNameChange}/>
                    </span>
                </FormGroup>
                <FormGroup>
                    <span>
                        <Input type="text" name="lastname" id="LastName" placeholder="Sukunimi" value = {this.state.last_name} onChange = {this.handleLastNameChange}/>
                    </span>
                </FormGroup>
                <FormGroup check>
                    <Label check className = "standard-text-color">
                        <Input type="checkbox" />{' '}
                        Ylläpitäjä
                    </Label>
                </FormGroup>

                <div className = "standard-text-color">Käyttäjälle lähetetään sähköpostiin linkki josta hän voi aktivoida tilin ja asettaa salasanan.</div>
                <br/>

                <FormGroup>

                    <Button className="btn btn-success" onClick={this.handleSubmit}>Luo</Button>

                </FormGroup>
                <br/>

            </Form>
        );
    }
}

export default AddUser;