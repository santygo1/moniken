import React, { useEffect } from "react";
import { useDispatch } from "react-redux";

function RouteFullView({ routeObject, closeModHandler, editModOnHandler }) {
  const dispatch = useDispatch();

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
      <p>Body:</p>
      {routeObject.body.map((item) => (
        <p>
          {item.name}: {item.value}
        </p>
      ))}
      <p>Headers:</p>
      {routeObject.headers.map((item) => (
        <p>
          {item.name}: {item.value}
        </p>
      ))}
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
              type: "deleteRout",
              payload: { id: routeObject.id },
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
