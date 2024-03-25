import { useNavigate } from "react-router-dom";
import Button from "./Button";

function BackButton({ extraStyle }) {
  const navigate = useNavigate();

  return (
    <Button extraStyle={extraStyle + " mx-1"} onClick={() => navigate(-1)}>
      Back
    </Button>
  );
}

export default BackButton;
