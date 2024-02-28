import i18next from "i18next";
import enFlag from '/src/assets/en.svg'; 
import ltFlag from '/src/assets/lt.svg';

const lngs = {
  en: { flag: enFlag },
  lt: { flag: ltFlag },
};

export default function LanguageSwitcher() {
  const currentLanguage = i18next.language;

  const handleChangeLanguage = (lng) => {
    i18next.changeLanguage(lng).then(() => {
      window.location.reload();
    });
  };
  
  return (
    <div className="flex">
      {Object.keys(lngs).map((lng) => (
        <button
          type="submit"
          key={lng}
          onClick={() => handleChangeLanguage(lng)}
          disabled={currentLanguage === lng}
          className={`flex items-center mx-1.5 border-blue-500 ${currentLanguage === lng ? 'border-2' : 'border-0' }`}
        >
          <img
            src={lngs[lng].flag}
            alt={lngs[lng].nativeName}
            width={40}
            
          />
        </button>
      ))}
    </div>
  );
}