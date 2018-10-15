import React from "react";

// Importing from reactstrap like this:
import { Collapse,
    Navbar,
    NavbarToggler,
    NavbarBrand,
    Nav,
    NavItem,
    NavLink,
    UncontrolledDropdown,
    DropdownToggle,
    DropdownMenu,
    Button,
    FormGroup,
    Label,
    Input,
    Form,
    DropdownItem } from 'reactstrap';
import'./Navigation.css';

class Navigation extends React.Component {
    constructor(props) {
        super(props);

        /*)   this.toggle = this.toggle.bind(this);
        this.state = {
            isOpen: false
        };
    }
    toggle() {
        this.setState({
            isOpen: !this.state.isOpen
        });
    }*/
        this.toggleNavbar = this.toggleNavbar.bind(this);
        this.state = {
            collapsed: true
        };
    }

    toggleNavbar() {
        this.setState({
            collapsed: !this.state.collapsed
        });
    }

    // Add functions for click event checks

    render() {
        return (
                <div>

                    <Navbar color="light" light  expand="sm">
                        <NavbarBrand href="/" className="mr-auto">Matsku</NavbarBrand>
                        <Form className = "nav-form">
                            <Input type="text" id="textSearch" placeholder="Hae tuotetta..." />
                            <Button type="submit" onClick={() => this.props.setView("articles")}>Hae</Button>
                        </Form>

                            <NavbarToggler onClick={this.toggleNavbar} className="mr-2" />
                            <Collapse isOpen={!this.state.collapsed} navbar>
                                <Nav classname="ml-auto" navbar>



                                  <UncontrolledDropdown nav inNavbar>

                                      <DropdownToggle nav caret>

                                      </DropdownToggle>
                            <DropdownMenu left>
                                <DropdownItem onClick={() => this.props.setView("profile")}>
                                    Profiili
                                </DropdownItem>
                                <DropdownItem onClick={() => this.props.setView("articles")}>
                                    Etusivu
                                </DropdownItem>
                                <DropdownItem onClick={() => this.props.setView("add-article")}>
                                    Lisää tuote
                                </DropdownItem>
                                <DropdownItem divider />
                                <DropdownItem onClick={() => this.props.setView("manage-users")}>
                                    Käyttäjien hallinta (Admin)
                                </DropdownItem>
                                <DropdownItem onClick={() => this.props.setView("audit-log")}>
                                    Tapahtumat (Admin)
                                </DropdownItem>
                            </DropdownMenu>
                        </UncontrolledDropdown>

                        <NavItem >
                            <NavLink onClick={() => this.props.setView("login")}>  Kirjaudu ulos</NavLink>
                        </NavItem>




                                </Nav>
                    </Collapse>

                </Navbar>
            </div>

        );
    }
}

export default Navigation;