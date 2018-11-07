import React from "react";
import { Form, Input, FormGroup, Label, Button} from "reactstrap";
import '../../css/Buttons.css';

class AddUser extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            first_name: '',
            last_name: '',
            email: '',
            admin_checkbox: false
        };
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
    }

/*
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
    }*/

handleChange(event) {
    const target = event.target;
    const value = target.type === 'checkbox' ? target.checked : target.value;
    const name = target.name;

    this.setState({
        [name]: value
    })
}

    handleSubmit() {
        this.props.addUser(this.state.email, this.state.first_name, this.state.last_name, this.state.admin_checkbox);
    }

    render() {
        return(
            <Form>
                <h5 className = "white-h5">Luo uusi käyttäjä</h5>

                <FormGroup>
                    <span>
                        <Input type="email" name="email" id="Email" placeholder="Sähköposti" value = {this.state.email} onChange = {this.handleChange}/>
                    </span>
                </FormGroup>
                <FormGroup>
                    <span>
                        <Input type="text" name="first_name" id="FirstName" placeholder="Etunimi" value = {this.state.first_name} onChange = {this.handleChange}/>
                    </span>
                </FormGroup>
                <FormGroup>
                    <span>
                        <Input type="text" name="last_name" id="LastName" placeholder="Sukunimi" value = {this.state.last_name} onChange = {this.handleChange}/>
                    </span>
                </FormGroup>

                <FormGroup check>
                    <Label check className = "standard-dark-text-color">
                        <Input type="checkbox" name="admin_checkbox" value = {this.state.admin_checkbox} onChange = {this.handleChange} />{' '}
                        Ylläpitäjä
                    </Label>
                </FormGroup>

                <div className = "standard-dark-text-color">Käyttäjälle lähetetään sähköpostiin linkki josta hän voi aktivoida tilin ja asettaa salasanan.</div>
                <br/>

                <FormGroup>

                    <Button className="btn-primary btn-wide" onClick={this.handleSubmit}>Luo</Button>

                </FormGroup>
                <br/>
                <h6 className = "standard-dark-text-color">{this.props.add_user_state}</h6>

            </Form>
        );
    }
}

export default AddUser;