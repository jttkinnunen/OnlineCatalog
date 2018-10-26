import React from "react";
import { Container, Row, Col } from 'reactstrap';
import './Footer.css';
class Footer extends React.Component {

    render() {
        return (
            <div className="event-text" id="fuutteri">
                <Container>
                    <Row>
                        <Col xs="6"><strong>FCG Talent Oy</strong></Col>
                        <Col xs="6"><strong>Tekijätiimi:</strong></Col>
                    </Row>
                    <Row>
                        <Col xs="6">Mikonkatu 7 93456 Hianola</Col>
                        <Col xs="6">Juppi</Col>
                    </Row>
                    <Row>
                        <Col xs="6">puh. 567 657987</Col>
                        <Col xs="6">Hippi</Col>
                    </Row>
                    <Row>
                        <Col xs="6">norsu.hiano@gmail.com</Col>
                        <Col xs="6">Punkkari</Col>
                    </Row>
                    <Row>
                        <Col xs="6">www.hiano.com</Col>
                        <Col xs="6">Juntti</Col>
                    </Row>
                </Container>
                {/*Current view: {this.props.current_view} Debug: {this.props.debugval}*/}
            </div>
        );
    }
}

export default Footer;