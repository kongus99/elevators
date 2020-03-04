module Container = {
  [@react.component] [@bs.module]
  external make: (~fluid: bool=?, ~children: React.element=?) => React.element =
    "react-bootstrap/Container";
};

module Row = {
  [@react.component] [@bs.module]
  external make: (~children: React.element=?) => React.element =
    "react-bootstrap/Row";
};

module Col = {
  [@react.component] [@bs.module]
  external make: (~lg: string=?, ~children: React.element=?) => React.element =
    "react-bootstrap/Col";
};

module Button = {
  [@react.component] [@bs.module]
  external make:
    (
      ~active: bool=?,
      ~block: bool=?,
      ~disabled: bool=?,
      ~size: string=?,
      ~variant: string=?,
      ~onClick: ReactEvent.Mouse.t => unit=?,
      ~children: React.element=?
    ) =>
    React.element =
    "react-bootstrap/Button";
};

module ButtonGroup = {
  [@react.component] [@bs.module]
  external make:
    (~size: string=?, ~vertical: bool=?, ~children: React.element=?) =>
    React.element =
    "react-bootstrap/ButtonGroup";
};

module ProgressBar = {
  [@react.component] [@bs.module]
  external make:
    (
      ~animated: bool=?,
      ~min: int=?,
      ~max: int=?,
      ~now: int=?,
      ~label: string=?,
      ~variant: string=?,
      ~children: React.element=?
    ) =>
    React.element =
    "react-bootstrap/ProgressBar";
};
