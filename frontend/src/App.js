import "./App.css";
import Sidebar from "./components/sidebar/Sidebar";
import Collection from "./components/collection/Collection";
import { useState } from "react";
import { Provider } from "react-redux";
import configureStore from "./store/index";

const store = configureStore();

function App() {
  const [currentCollectionId, setCurrentCollectionId] = useState({});

  const [detailId, setDetailId] = useState("");
  const setDetailIdHandler = (id) => setDetailId(id);

  const setCollectionHandler = (id) => {
    setDetailIdHandler("");
    setCurrentCollectionId(id);
  };

  return (
    <Provider store={store}>
      <div className="App">
        <main>
          <Sidebar collectionHandler={setCollectionHandler} />
          <Collection
            currentCollectionId={currentCollectionId}
            setDetailIdHandler={setDetailIdHandler}
            detailId={detailId}
          />
        </main>
      </div>
    </Provider>
  );
}

export default App;
