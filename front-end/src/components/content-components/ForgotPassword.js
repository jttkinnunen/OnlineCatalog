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
    }

    handleEmailChange(event) {
        this.setState({
            email_address: event.target.value,
        })
    }


    handleSubmit() {
        this.props.setView("login");
    }

    render() {
        return(
            <Jumbotron>
                <Form>
                    <h5 className = "white-h5">Unohdin salasanani :(</h5>
                    <p>
                        <h6 className = "white-h6">Ei hätää, kirjoita tunnukseesi liitetty sähköpostiosoite alle niin lähetämme sinulle linkin salasanan resetoimista varten.</h6>
                    </p>


                    <FormGroup>
                        <span>
                            <Input type="email" name="email" id="Email" placeholder="Sähköpostiosoite" value = {this.state.email_address} onChange = {this.handleEmailChange}/>
                        </span>
                    </FormGroup>
                    <br/>
                    <FormGroup className = "left-buttons">
                        <Button className="btn btn-success" onClick={() => {this.handleSubmit()}}>Lähetä</Button>
                    </FormGroup>
                    <br/>

                </Form>
            </Jumbotron>
        );
    }
}

export default ForgotPassword;