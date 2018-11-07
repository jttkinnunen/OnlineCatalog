import React from "react";
import { Form, Input, FormGroup, Label, Button} from "reactstrap";

class AddUser extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            first_name: '',
            last_name: '',
            email: '',
            admin: false
        };
        this.handleFirstNameChange = this.handleFirstNameChange.bind(this);
        this.handleLastNameChange = this.handleLastNameChange.bind(this);
        this.handleEmailChange = this.handleEmailChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleAdminChange = this.handleAdminChange.bind(this);
    }

    handleFirstNameChange(event) {
        this.setState({
            first_name: event.target.value,
        })
    }

    handleLastNameChange(event) {
        this.setState({
            last_name: event.target.value,
        })
    }

    handleEmailChange(event) {
        this.setState({
            email: event.target.value,
        })
    }

    handleAdminChange(event) {
        this.setState({
            admin: event.target.value,
        })
    }

    handleSubmit() {
        this.props.addUser(this.state.email, this.state.first_name, this.state.last_name, this.state.admin);
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
                    <Label check className = "standard-dark-text-color">
                        <Input type="checkbox" value = {this.state.admin} onChange = {this.handleAdminChange} />{' '}
                        Ylläpitäjä
                    </Label>
                </FormGroup>

                <div className = "standard-dark-text-color">Käyttäjälle lähetetään sähköpostiin linkki josta hän voi aktivoida tilin ja asettaa salasanan.</div>
                <br/>

                <FormGroup>

                    <Button className="btn btn-success" onClick={this.handleSubmit}>Luo</Button>

                </FormGroup>
                <br/>
                <h6 className = "standard-dark-text-color">{this.props.add_user_state}</h6>

            </Form>
        );
    }
}

export default AddUser;