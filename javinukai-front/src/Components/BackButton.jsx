import { useNavigate } from "react-router-dom";
import Button from "./Button";
import { useTranslation } from "react-i18next";

function BackButton({ extraStyle }) {
  const { t } = useTranslation();
  const navigate = useNavigate();

  return (
    <Button extraStyle={extraStyle + " mx-1"} onClick={() => navigate(-1)}>
      {t("UserDetailsPage.backButton")}
    </Button>
  );
}

export default BackButton;
