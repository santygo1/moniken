import React, { useState } from "react";
import { Formik, Form, Field, FieldArray, ErrorMessage } from "formik";
import { useDispatch } from "react-redux";

function AddNewCollectionTab({ closeHandle, currentCollectionId }) {
  const initialValues = {
    name: "",
    endpoint: "",
    status: "",
    body: [{ name: "", value: "" }],
    headers: [{ name: "", value: "" }],
    timeout: "",
    description: "",
    method: "",
  };

  const dispatch = useDispatch();

  const handleSubmit = (values) => {
    if (values.method === "") {
      values.method = "GET";
    }
    dispatch({
      type: "createRout",
      payload: { ...values, collectionId: currentCollectionId },
    });
    closeHandle();
  };

  return (
    <div className="container px-4 py-5">
      <h2>New endpoint</h2>

      <Formik initialValues={initialValues} onSubmit={handleSubmit}>
        {({ values }) => (
          <Form className="route-form">
            <label htmlFor="name" className="form-label">
              Name
            </label>
            <Field
              className="form-control"
              type="text"
              name="name"
              placeholder="route name"
              id="name"
            />
            <label htmlFor="endpoint" className="form-label">
              Endpoint
            </label>
            <Field
              className="form-control"
              type="text"
              name="endpoint"
              placeholder="/.../..."
              id="endpoint"
            />
            <div>Headers</div>
            <FieldArray name="headers">
              {({ push, remove }) => (
                <div>
                  {values.headers.map((_, index) => (
                    <div key={index} className="d-flex">
                      <Field
                        className="form-control"
                        type="text"
                        placeholder="prop name"
                        name={`headers.[${index}].name`}
                      />
                      <Field
                        className="form-control"
                        type="text"
                        placeholder="prop value"
                        name={`headers.[${index}].value`}
                      />
                      <button
                        className="btn btn-secondary"
                        type="button"
                        onClick={() => remove(index)}
                      >
                        Удалить
                      </button>
                    </div>
                  ))}
                  <button
                    className="btn btn-secondary"
                    type="button"
                    onClick={() => push({ name: "", value: "" })}
                  >
                    Add header
                  </button>
                </div>
              )}
            </FieldArray>

            <Field className="form-select" name="method" component="select">
              <option value="GET" selected>
                GET
              </option>
              <option value="PUT">PUT</option>
              <option value="POST">POST</option>
              <option value="DELETE">DELETE</option>
              <option value="OPTIONS">OPTIONS</option>
              <option value="HEAD">HEAD</option>
              <option value="PATCH">PATCH</option>
            </Field>
            <Field
              className="form-control"
              type="text"
              name="status"
              placeholder="status"
            />
            <Field
              className="form-control"
              type="number"
              name="timeout"
              placeholder="timeout"
            />
            <div>Body props</div>
            <FieldArray name="body">
              {({ push, remove }) => (
                <div>
                  {values.body.map((_, index) => (
                    <div key={index} className="d-flex">
                      <Field
                        className="form-control"
                        type="text"
                        placeholder="prop name"
                        name={`body.[${index}].name`}
                      />
                      <Field
                        className="form-control"
                        type="text"
                        placeholder="prop value"
                        name={`body.[${index}].value`}
                      />
                      <button
                        className="btn btn-secondary"
                        type="button"
                        onClick={() => remove(index)}
                      >
                        Удалить
                      </button>
                    </div>
                  ))}
                  <button
                    className="btn btn-secondary"
                    type="button"
                    onClick={() => push({ name: "", value: "" })}
                  >
                    Add props
                  </button>
                </div>
              )}
            </FieldArray>

            <Field
              className="form-control"
              type="text"
              as="textarea"
              name="description"
              placeholder="description"
            />

            <button className="btn btn-secondary" type="submit">
              Invite
            </button>
          </Form>
        )}
      </Formik>

      <button onClick={closeHandle}>Отменить</button>
    </div>
  );
}

export default AddNewCollectionTab;
