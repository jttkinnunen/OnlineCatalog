import React from "react";
import { Form, Input, FormGroup, Button, Jumbotron} from "reactstrap";
import queryString from 'query-string';
import postJsonRequest from '../../App.js';


// TODO: What if user tries to activate multiple times? proper checks and output missing
// TODO: prepare for cases where user by this id does not exist etc. coordinate with back-end

class ActivateUser extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            pass_first: '',
            pass_second: '',
            activation_result: '',
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
        if (this.state.pass_first === this.state.pass_second) {

            this.setState({
                pass_first: '',
                pass_second: '',
                activation_result: 'Aktivoidaan...'
            });

            postJsonRequest('/setPassword',
                    {
                        newPassword: this.state.pass_first,
                        resetToken: queryString.parse(this.props.location.search).key,
                    }
                ).then((responseJson) => {

                    // TODO: ota vastauksesta tieto onnistuiko
                    // responseJson.jotain
                    // if (responseJson.hasOwnProperty('jokuelementti'))

                    this.setState({
                        pass_first: "",
                        pass_second: ""
                    });

                })
                .catch(err => {
                    this.setState({
                        debugval: this.state.debugval + " Error-fetching-articles:" + err,
                        activation_result: 'Yhteysongelma'
                    })
                })
        }
        else
            this.setState({
                activation_result: 'Salasanat eivät täsmää'
            });
    }

    render() {
        return(
            <div className="App App-header">
                    <Jumbotron className = "align-self-center">
                        <Form>
                            <h5 className = "white-h5">Aktivoi tunnuksesi</h5>
                            <p>
                                <h6 className = "white-h6">Tervetuloa käyttämään Matskua, Placeholder! Aseta itsellesi salasana ja pääset heti hommiin.</h6>
                                <div className = "white-h6">(Testiä varten) avain: {queryString.parse(this.props.location.search).key} </div>
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
            </div>
        );
    }
}

export default ActivateUser;