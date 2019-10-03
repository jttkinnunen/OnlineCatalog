import 'bootstrap/dist/css/bootstrap.css';
import React from 'react';
import { render } from 'react-dom'
import './css/index.css';
import'../node_modules/font-awesome/css/font-awesome.min.css';
import App from './App';
import * as serviceWorker from './serviceWorker';
import { Route, BrowserRouter  } from 'react-router-dom'
import ActivateUser from './components/content-components/ActivateUser.js';

render((
    <BrowserRouter>
        <div>
            <Route path="/activate" component={ActivateUser}/>
            <Route exact path="/" component={App}/>
        </div>
    </BrowserRouter>
), document.getElementById('root'));

//ReactDOM.render(<App />, document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: http://bit.ly/CRA-PWA
serviceWorker.unregister();
