import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import CollectSideView from "./CollectSideView";

function Sidebar({ collectionHandler, setDetailIdHandler }) {
  const collections = useSelector(
    (state) => state.collectionReducer.collections,
  );

  const dispatch = useDispatch();

  const [newCollectionName, setNewCollectionName] = useState("");
  const [newCollectionDesc, setNewCollectionDesc] = useState("");

  useEffect(() => {
    dispatch({ type: "getAllCollections" });
  }, []);

  return (
    <div className="sidebar d-flex flex-column flex-shrink-0 p-3 bg-light">
      <div className="d-flex align-items-center mb-3 mb-md-0 me-md-auto link-dark text-decoration-none">
        <span className="fs-4">Moniken (v 0.0.1)</span>
      </div>
      <hr></hr>
      <h3>Collections</h3>
      <hr></hr>
      <ul className="nav nav-pills flex-column mb-auto">
        {collections.map((item) => (
          <li className="nav-item">
            <CollectSideView
              name={item.name}
              key={item.id}
              id={item.id}
              collectionHandler={collectionHandler}
            />
          </li>
        ))}
        <li>
          <div className="nav-link link-dark">
            <button
              type="button"
              className="btn btn-secondary"
              data-bs-toggle="collapse"
              data-bs-target="#collapseForm"
              aria-expanded="false"
              aria-controls="collapseForm"
            >
              +
            </button>
            <div className="collapse pt-2" id="collapseForm">
              <form>
                <input
                  value={newCollectionName}
                  onChange={(e) => setNewCollectionName(e.target.value)}
                  className="new-coll form-control-sm w-75"
                  type="text"
                  placeholder="Name"
                ></input>
                <textarea
                  value={newCollectionDesc}
                  onChange={(e) => setNewCollectionDesc(e.target.value)}
                  className="form-control-sm"
                  placeholder="Description"
                ></textarea>
              </form>
              <button
                type="button"
                className="btn btn-primary"
                onClick={() => {
                  dispatch({
                    type: "createColl",
                    payload: {
                      name: newCollectionName,
                      description: newCollectionDesc,
                    },
                  });
                }}
              >
                +
              </button>
            </div>
          </div>
        </li>
      </ul>
    </div>
  );
}

export default Sidebar;
