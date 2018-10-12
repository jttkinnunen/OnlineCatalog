import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import Body from './components/Body.js';
import Events from './components/Events.js';
import Navigation_bar from './components/Navigation.js';

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
                    <div class="container-fluid" className = "navigation-bar">
                        <Navigation_bar />
                    </div>

                    <div class="container-fluid" className = "event-bar">
                        <Events />
                    </div>

                    <div class="container-fluid" className = "body">
                        <Body />
                    </div>

                    <img src={logo} className="App-logo" alt="logo" />

                </header>
            </div>
        );
    }
}

export default App;
