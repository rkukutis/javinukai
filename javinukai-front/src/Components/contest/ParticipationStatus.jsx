import { useTranslation } from "react-i18next";

export function ParticipationStatus({ status }) {
  const { t } = useTranslation();
  const styles = {
    PENDING: ["bg-yellow-500", t(`ParticipationStatus.PENDING`)],
    ACCEPTED: ["bg-green-500", t(`ParticipationStatus.ACCEPTED`)],
    DECLINED: ["bg-red-500", t(`ParticipationStatus.DECLINED`)],
  };

  return (
    <>
      {status && (
        <span className={`p-2 text-white rounded ${styles[status][0]}`}>
          {styles[status][1]}
        </span>
      )}
    </>
  );
}
