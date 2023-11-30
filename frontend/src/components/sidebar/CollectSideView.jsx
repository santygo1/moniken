import React from "react";

function CollectSideView({ name, id, collectionHandler }) {
  return (
    <button
      type="button"
      className="nav-link link-dark collect-side-view"
      onClick={() => collectionHandler(id)}
    >
      {name}
    </button>
  );
}

export default CollectSideView;
