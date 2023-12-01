import React, { useState } from "react";
import { useSelector } from "react-redux";
import AddNewCollectionTab from "./AddNewCollectionTab";

function Collection({ currentCollectionId }) {
  const collections = useSelector(
    (state) => state.collectionReducer.collections,
  );
  const currentCollection = collections.find(
    (item) => item.id === currentCollectionId,
  );

  const [isNewModOn, setNewMod] = useState(false);

  if (!currentCollection) {
    return (
      <div className="collection container px-4 py-5">
        <h2 className="pb-2 border-bottom">Выберете коллекцию</h2>
      </div>
    );
  }

  if (isNewModOn) {
    return <AddNewCollectionTab closeHandle={() => setNewMod(false)} />;
  }

  return (
    <div className="collection container px-4 py-5" id="featured-3">
      <h2 className="pb-2 border-bottom">{currentCollection.name}</h2>
      <div className="row g-xl-5 py-5 row-cols-1 row-cols-lg-3">
        <div className="feature col">
          <div>POST</div>
          <h2>/cool_users/666</h2>
          <p>Params, body, header, e.t.c...</p>
          <a href="#" className="icon-link">
            Редактировать
            <svg className="bi" width="1em" height="1em">
              <use xlinkHref="#chevron-right"></use>
            </svg>
          </a>
        </div>

        <div className="feature col btn-block">
          <button
            className="new-route-btn"
            onClick={() => {
              setNewMod(true);
            }}
          >
            Add new route
          </button>
        </div>
      </div>
    </div>
  );
}

export default Collection;
