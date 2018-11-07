import React from "react";
import { Form, Input, FormGroup, Label, Button, Jumbotron} from "reactstrap";
import '../../css/ChangePassword.css';
import '../../css/Text.css';

class ChangePassword extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            pass_first: '',
            pass_second: '',
            pass_old: ''
        };
        this.handlePassOneChange = this.handlePassOneChange.bind(this);
        this.handlePassTwoChange = this.handlePassTwoChange.bind(this);
        this.handleOldPassChange = this.handleOldPassChange.bind(this);
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

    handleOldPassChange(event) {
        this.setState({
            pass_old: event.target.value,
        })
    }

    // TODO: Actually send
    // TODO: Print errors like in login page, if password mismatch or if requirements not met
    handleSubmit() {
        if (this.state.pass_first === this.state.pass_second) {

            this.setState({
                pass_first: '',
                pass_second: '',
                pass_old: ''
            });
        }
    }

    render() {
        return(

            <Jumbotron id="changeword">
                <Form>
                    <h5 className = "white-h5">Vaihda salasana</h5>
                    <p>
                        <h6 className = "white-h6">Syötä ensin vanha salasanasi, ja sitten uusi salasana kaksi kertaa</h6>
                    </p>

                    <FormGroup>
                        <span>
                            <Input type="password" name="pass1" id="Pass1" placeholder="Vanha salasana" value = {this.state.pass_old} onChange = {this.handlePassOneChange}/>
                        </span>
                    </FormGroup>
                    <FormGroup>
                        <span>
                            <Input type="password" name="pass2" id="Pass2" placeholder="Uusi salasana" value = {this.state.pass_first} onChange = {this.handlePassTwoChange}/>
                        </span>
                    </FormGroup>
                    <FormGroup>
                        <span>
                            <Input type="password" name="pass2" id="Pass2" placeholder="Uusi salasana uudestaan" value = {this.state.pass_second} onChange = {this.handlePassTwoChange}/>
                        </span>
                    </FormGroup>
                    <br/>

                    <FormGroup>

                        <Button  id="passbutton" onClick={this.handleSubmit}>Vaihda salasana</Button>

                    </FormGroup>
                    <br/>

                </Form>
            </Jumbotron>
        );
    }
}

export default ChangePassword;