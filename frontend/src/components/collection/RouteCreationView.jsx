import React from "react";
import { useDispatch, useSelector } from "react-redux";
import RouteForm from "./RouteForm";
import { expandInitialValues } from "../../utils/formData";

function RouteCreationView({
  closeHandle,
  initialValues,
  dispatchType,
  collectionName,
}) {
  const dispatch = useDispatch();
  const routeObject = useSelector((state) => state.routesReducer.currentRoute);

  const handleSubmit = (values) => {
    console.log("values", values);
    if (values.method === "") {
      values.method = "GET";
    }
    dispatch({
      type: dispatchType,
      payload: values,
      collection: collectionName,
      routeId: routeObject ? routeObject.id : "",
    });
    closeHandle();
  };

  if (dispatchType === "updateRouteById") {
    return (
      <div className="container px-4 py-5">
        <h2>Edit endpoint</h2>

        <RouteForm
          initialValues={expandInitialValues(routeObject)}
          handleSubmit={handleSubmit}
        />

        <button onClick={closeHandle}>Отменить</button>
      </div>
    );
  }

  return (
    <div className="container px-4 py-5">
      <h2>New endpoint</h2>

      <RouteForm initialValues={initialValues} handleSubmit={handleSubmit} />

      <button onClick={closeHandle}>Отменить</button>
    </div>
  );
}

export default RouteCreationView;
