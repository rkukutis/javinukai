import { useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import confirmEmail from "../services/auth/confirmEmail";
import { useTranslation } from "react-i18next";

function ConfirmEmailPage() {
  const { t } = useTranslation();
  const [searchParams] = useSearchParams();
  console.log(searchParams.get("token"));

  useEffect(
    function () {
      if (!searchParams.get("token")) return;
      confirmEmail(searchParams.get("token"));
    },
    [searchParams]
  );

  return <div>{t('ConfirmEmailPage.emailConfirmed')}</div>;
}

export default ConfirmEmailPage;
