import React from "react";
import { Form, Input, FormGroup, Label, Button, Jumbotron} from "reactstrap";

class ActivateUser extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            pass_first: '',
            pass_second: '',
        };
        this.handlePassOneChange = this.handlePassOneChange.bind(this);
        this.handlePassTwoChange = this.handlePassTwoChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handlePassOneChange(event) {
        this.setState({
            pass_first: event.target.value,
        })
    }

    handlePassTwoChange(event) {
        this.setState({
            pass_second: event.target.value,
        })
    }

    handleSubmit() {
        this.props.setView("login");
    }

    render() {
        return(
            <Jumbotron>
                <Form>
                    <h5 className = "white-h5">Aktivoi tunnuksesi</h5>
                    <p>
                        <h6 className = "white-h6">Tervetuloa käyttämään Matskua, Placeholder! Aseta itsellesi salasana ja pääset heti hommiin.</h6>
                    </p>

                    <FormGroup>
                        <span>
                            <Input type="password" name="pass1" id="Pass1" placeholder="Salasana" value = {this.state.pass_first} onChange = {this.handlePassOneChange}/>
                        </span>
                    </FormGroup>
                    <FormGroup>
                        <span>
                            <Input type="password" name="pass2" id="Pass2" placeholder="Salasana uudestaan" value = {this.state.pass_second} onChange = {this.handlePassTwoChange}/>
                        </span>
                    </FormGroup>
                    <br/>

                    <FormGroup>

                        <Button className="btn btn-success" onClick={this.handleSubmit}>Aktivoi</Button>

                    </FormGroup>
                    <br/>

                </Form>
            </Jumbotron>
        );
    }
}

export default ActivateUser;