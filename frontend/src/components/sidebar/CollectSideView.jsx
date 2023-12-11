import React from "react";
import { XLg } from "react-bootstrap-icons";
import styles from "./CollectSideView.module.css";
import { useDispatch } from "react-redux";
function CollectSideView({ name, id, collectionHandler }) {
  const dispatch = useDispatch();

  return (
    <div className={styles.collectSideView}>
      <div
        className="nav-link link-dark collect-side-view"
        onClick={() => collectionHandler(id)}
      >
        {name}
      </div>
      <XLg
        onClick={() => {
          dispatch({ type: "deleteCollectionByName", payload: name });
        }}
      />
    </div>
  );
}

export default CollectSideView;
