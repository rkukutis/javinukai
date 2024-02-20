import { useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import confirmEmail from "../services/auth/confirmEmail";

function ConfirmEmailPage() {
  const [searchParams] = useSearchParams();
  console.log(searchParams.get("token"));

  useEffect(
    function () {
      if (!searchParams.get("token")) return;
      confirmEmail(searchParams.get("token"));
    },
    [searchParams]
  );

  return <div>YOUR EMAIL HAS BEEN CONFIRMED</div>;
}

export default ConfirmEmailPage;
