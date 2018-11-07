import React from "react";
import { Form, Input, FormGroup,  Button, Jumbotron} from "reactstrap";

class ForgotPassword extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            email_address: '',
        };
        this.handleEmailChange = this.handleEmailChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleBack = this.handleBack.bind(this);
    }

    handleEmailChange(event) {
        this.setState({
            email_address: event.target.value,
        })
    }

    handleSubmit() {
        if (this.state.email_address.length > 3) {
            this.props.forgotPassword(this.state.email_address);
            this.setState({
                email_address: "",
            })
        }
    }

    handleBack() {
        this.props.setView("login");
    }

    render() {
        return(

                <Form>
                    <h5 className = "white-h5">Unohdin salasanani :(</h5>
                    <p>
                        <h6 className = "white-h6">Ei hätää, kirjoita sähköpostiosoitteesi alle niin lähetämme sinulle linkin salasanan vaihtamista varten.</h6>
                    </p>


                    <FormGroup>
                        <span>
                            <Input type="email" name="email" id="Email" placeholder="Sähköpostiosoite" value = {this.state.email_address} onChange = {this.handleEmailChange}/>
                        </span>
                    </FormGroup>
                    <br/>
                    <FormGroup className = "left-buttons">
                        <Button className="btn btn-success" onClick={() => {this.handleSubmit()}}>Lähetä</Button>
                        <Button className="btn" onClick={() => {this.handleBack()}}>Takaisin</Button>
                    </FormGroup>
                    <br/>
                    <h6 className = "standard-text-color">{this.props.pass_reset_state}</h6>
                </Form>

        );
    }
}

export default ForgotPassword;