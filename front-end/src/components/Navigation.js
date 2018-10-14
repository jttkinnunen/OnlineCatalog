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
                            <Button type="submit">Hae</Button>
                        </Form>

                            <NavbarToggler onClick={this.toggleNavbar} className="mr-2" />
                            <Collapse isOpen={!this.state.collapsed} navbar>
                                <Nav classname="ml-auto" navbar>



                                  <UncontrolledDropdown nav inNavbar>

                                      <DropdownToggle nav caret>

                                      </DropdownToggle>
                            <DropdownMenu left>
                                <DropdownItem>
                                    Profiili
                                </DropdownItem>
                                <DropdownItem>
                                    Etusivu
                                </DropdownItem>
                                <DropdownItem>
                                    Lisää tuote
                                </DropdownItem>
                                <DropdownItem divider />
                                <DropdownItem>
                                    Käyttäjien hallinta (Admin)
                                </DropdownItem>
                                <DropdownItem>
                                    Tapahtumat (Admin)
                                </DropdownItem>
                            </DropdownMenu>
                        </UncontrolledDropdown>

                        <NavItem >
                            <NavLink href="/">  Kirjaudu ulos</NavLink>
                        </NavItem>




                                </Nav>
                    </Collapse>

                </Navbar>
            </div>

        );
    }
}

export default Navigation;