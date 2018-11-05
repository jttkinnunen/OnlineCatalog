import React from "react";
import { Form, Input, FormGroup, Button, Jumbotron} from "reactstrap";
import queryString from 'query-string';


// TODO: What if user tries to activate multiple times? proper checks and output missing
// TODO: prepare for cases where user by this id does not exist etc. coordinate with back-end

const HOST = "http://localhost:8080";

// Status messages
const P_PROGRESS = "Aktivoidaan ...";
const P_SUCCESS = "Tehty. Voit nyt kirjautua Matsku palvelussa uudella salasanallasi.";
const P_ERR_CONNECTION = "Yhteysongelma";
const P_TOO_SHORT = "Salasanan tulee olla vähintään 6 merkkiä pitkä";
const P_AUTH_ERROR = "Tunnus on jo aktivoitu tai sitä ei löytynyt tietokannasta";



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
        this.postJsonRequest = this.postJsonRequest.bind(this);
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

    postJsonRequest(path, payload){
        return(
            fetch(HOST + path, {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(payload),
            }))
            .then(response => response.json());
    }

    handleSubmit() {

        // Check password length
        if (this.state.pass_first.length < 6){
            this.setState({activation_result: P_TOO_SHORT});
            return;
        }


        if (this.state.pass_first === this.state.pass_second) {

            this.setState({
                pass_first: '',
                pass_second: '',
                activation_result: P_PROGRESS
            });

            this.postJsonRequest('/setPassword',
                    {
                        newPassword: this.state.pass_first,
                        reset_token: queryString.parse(this.props.location.search).key,
                    }
                ).then((responseJson) => {

                    if (responseJson.hasOwnProperty('error')){
                        if (responseJson.error === "Authentication Error")
                            this.setState({activation_result: P_AUTH_ERROR});
                    }
                    else
                        {
                            // Success

                        this.setState({
                            pass_first: "",
                            pass_second: "",
                            activation_result: P_SUCCESS, // TODO: Ei muuten oo välttämättä aktivoitu. tarkista mitä backend vastasi.
                        });
                        // TODO: siirry esim. 5 sek siirtymällä login sivulle jos onnistui
                        }

                })
                .catch(err => {
                    this.setState({
                        debugval: this.state.debugval + " Error-fetching-articles:" + err,
                        activation_result: P_ERR_CONNECTION,
                        pass_first: "",
                        pass_second: ""
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
                                <h6 className = "white-h6">Tervetuloa käyttämään Matskua! Aseta itsellesi salasana ja pääset heti hommiin.</h6>
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
                            <h6 className = "standard-text-color">{this.state.activation_result}</h6>
                            <br/>
                            <a href="/">Tästä pääset Matsku palveluun</a>
                        </Form>
                    </Jumbotron>
            </div>
        );
    }
}

export default ActivateUser;