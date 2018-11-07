import React from "react";
import { Col, Button, Form, FormGroup, Card, CardImg} from 'reactstrap';
import '../../css/ProfilePage.css';
import Jumbotron from "react-bootstrap/es/Jumbotron";
class ProfilePage extends React.Component {
    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit() {
        this.props.setView("change-pass");
    }

    // TODO: Linkkaa
    render() {
        let userType;

        if (this.props.user.admin === true)
            {userType = <h6 className="standard-text-color">Ylläpitäjän oikeudet</h6>}
        else
            {userType = <h6 className="standard-text-color">Peruskäyttäjän oikeudet</h6>}

        return(
            <div className="profiles">
                <Jumbotron>

                    <h2 className = "profileheader" >Oma profiili</h2>
                    <br/> <br/>
                    <h5 className="standard-text-color">{this.props.user.first_name} {this.props.user.last_name}</h5>
                    <br/>
                    <h5 className="standard-text-color">{this.props.user.email}</h5>
                    <br/>
                    <h6 className="standard-text-color">{userType}</h6>
                    <br/><br/><br/>
                    <Button id="changepass" onClick={this.handleSubmit}>Vaihda salasana</Button>
                </Jumbotron>
            </div>
        );
    }
}

export default ProfilePage;