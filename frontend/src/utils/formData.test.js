import { bodyHeadTransformer, bodyHeadToArray } from "./formData";

describe("Test function bodyHeadTransformer", () => {
  test("correct", () => {
    expect(
      bodyHeadTransformer([
        { name: "first", value: "123" },
        { name: "second", value: "456" },
      ]),
    ).toEqual({
      first: "123",
      second: "456",
    });
  });
  test("empty", () => {
    expect(bodyHeadTransformer([])).toEqual({});
  });
  test("not correct data", () => {
    expect(
      bodyHeadTransformer([
        { name: "first", value: "123" },
        { name: "", value: "" },
      ]),
    ).toEqual({ first: "123" });
  });
  test("not correct data", () => {
    expect(
      bodyHeadTransformer([
        { name: "first", value: "123" },
        { name: "", value: "" },
      ]),
    ).toEqual({ first: "123" });
  });
});

describe("Test function bodyHeadToArray", () => {
  test("correct", () => {
    expect(
      bodyHeadToArray({
        first: "123",
        second: "456",
      }),
    ).toEqual([
      { name: "first", value: "123" },
      { name: "second", value: "456" },
    ]);
  });
});
