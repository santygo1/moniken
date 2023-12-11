import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";

function RouteFullView({ closeModHandler, editModOnHandler, routeId }) {
  const dispatch = useDispatch();
  const routeObject = useSelector((state) => state.routesReducer.currentRoute);

  useEffect(() => {
    dispatch({ type: "getRouteById", payload: routeId });
  }, [dispatch, routeId]);

  if (!routeObject) {
    return <h2>Loading</h2>;
  }

  return (
    <div>
      <button type="button" onClick={closeModHandler}>
        Назад
      </button>
      <h2>{routeObject.name}</h2>
      <hr></hr>
      <p>
        {routeObject.method} <strong>{routeObject.endpoint}</strong>
      </p>
      <hr></hr>
      {routeObject.status && <p>Status: {routeObject.status}</p>}
      {routeObject.timeout && <p>Timeout: {routeObject.timeout}</p>}
      {routeObject.body && (
        <div>
          <p>Body:</p>
          {Object.entries(routeObject.body).map(([key, value]) => (
            <p>
              {key}: {value}
            </p>
          ))}
        </div>
      )}
      {routeObject.headers && (
        <div>
          <p>Headers:</p>
          {Object.entries(routeObject.headers).map(([key, value]) => (
            <p>
              {key}: {value}
            </p>
          ))}
        </div>
      )}
      <hr></hr>
      <p>Description</p>
      {routeObject.description ? (
        <p>{routeObject.description}</p>
      ) : (
        <p>There is no description</p>
      )}
      <div>
        <button type="button" onClick={editModOnHandler}>
          Редактировать
        </button>
        <button
          type="button"
          onClick={() => {
            dispatch({
              type: "deleteRouteById",
              payload: routeObject.id,
            });
            closeModHandler();
          }}
        >
          Удалить
        </button>
      </div>
    </div>
  );
}

export default RouteFullView;
