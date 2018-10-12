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

class Navigation extends React.Component {
    constructor(props) {
        super(props);

        this.toggle = this.toggle.bind(this);
        this.state = {
            isOpen: false
        };
    }
    toggle() {
        this.setState({
            isOpen: !this.state.isOpen
        });
    }

    // Add functions for click event checks

    render() {
        return (
                <Navbar color="light" light  expand="sm">
                    <Nav navbar>
                        <UncontrolledDropdown nav inNavbar>
                            <DropdownToggle nav caret>
                            </DropdownToggle>
                            <DropdownMenu left>
                                <DropdownItem>
                                    Option 1
                                </DropdownItem>
                                <DropdownItem>
                                    Option 2
                                </DropdownItem>
                                <DropdownItem divider />
                                <DropdownItem>
                                    Reset
                                </DropdownItem>
                            </DropdownMenu>
                        </UncontrolledDropdown>

                        <NavItem>
                            <NavLink href="/">Matsku</NavLink>
                        </NavItem>

                        <Form className = "flex-nav-form">
                            <Input type="text" id="textSearch" placeholder="Hae tuotetta..." />
                            <Button type="submit">Hae</Button>
                        </Form>

                        <UncontrolledDropdown nav inNavbar>
                            <DropdownToggle nav caret>
                                Keijo Käyttäjä
                            </DropdownToggle>
                            <DropdownMenu right>
                                <DropdownItem>
                                    Oma profiili
                                </DropdownItem>
                                <DropdownItem>
                                    Hallitse käyttäjiä (Admin)
                                </DropdownItem>
                                <DropdownItem>
                                    Tapahtumat (Admin)
                                </DropdownItem>
                                <DropdownItem divider />
                                <DropdownItem>
                                    Kirjaudu ulos
                                </DropdownItem>
                            </DropdownMenu>
                        </UncontrolledDropdown>

                    </Nav>
                </Navbar>
        );
    }
}

export default Navigation;