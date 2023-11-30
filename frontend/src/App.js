import logo from "./logo.svg";
import "./App.css";
import Sidebar from "./components/sidebar/Sidebar";
import Collection from "./components/collection/Collection";
import { useState } from "react";
import { createStore } from "redux";
import rootReducer from "./reducers/index";
import { Provider } from "react-redux";

const store = createStore(rootReducer);

function App() {
  const [currentCollectionId, setCurrentCollectionId] = useState({});

  const setCollectionHandler = (id) => {
    setCurrentCollectionId(id);
  };

  return (
    <Provider store={store}>
      <div className="App">
        <main>
          <Sidebar collectionHandler={setCollectionHandler} />
          <Collection currentCollectionId={currentCollectionId} />
        </main>
      </div>
    </Provider>
  );
}

export default App;
