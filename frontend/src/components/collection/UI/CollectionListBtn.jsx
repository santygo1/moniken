import React from "react";

function CollectionListBtn({ description, onClickHandler }) {
  return (
    <div className="feature col btn-block">
      <button className="new-route-btn" onClick={onClickHandler}>
        {description}
      </button>
    </div>
  );
}

export default CollectionListBtn;
