import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import Body from './Components/Body.js';
import Events from './Components/Events.js';
import Navigation_bar from './Components/Navigation.js';

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            // Tähän kaikki mahdolliset muuttujat mitä sivulla voi olla. Päivitetään alielementeille tarvittaessa.

            user: null,
            user_token: null,
            user_rights: null,
            articles: Array(50).fill(null),
            events: Array(50).fill(null),
        };
    }

    render() {
        return (
            <div className="App">

                <header className="App-header">
                    <div>
                        <Navigation_bar />
                    </div>

                    <div>
                        <Events />
                    </div>

                    <div>
                        <Body />
                    </div>

                    <img src={logo} className="App-logo" alt="logo" />

                </header>
            </div>
        );
    }
}

export default App;
