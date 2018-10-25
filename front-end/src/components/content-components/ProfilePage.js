import React from "react";
import { Col, Button, Form, FormGroup, Card, CardImg} from 'reactstrap';

class ProfilePage extends React.Component {
    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit() {
        this.props.setView("change-password");
    }

    // TODO: Linkkaa
    render() {
        let userType;

        if (this.props.user.admin === true)
            {userType = <h6 className="standard-text-color">Ylläpitäjän oikeudet</h6>}
        else
            {userType = <h6 className="standard-text-color">Peruskäyttäjän oikeudet</h6>}

        return(
            <div>
                <h6 className="standard-text-color">{this.props.user.first_name} {this.props.user.last_name}</h6>
                <h6 className="standard-text-color">{this.props.user.email}</h6>
                <h6 className="standard-text-color">{userType}</h6>

                <Form>
                    <FormGroup check row>
                            <Button onClick={this.handleSubmit}>Vaihda salasana</Button>
                    </FormGroup>
                </Form>
            </div>
        );
    }
}

export default ProfilePage;