// @flow

export type CardParameters = {
    number: string,
    expirationMonth: string,
    expirationYear: string,
    cvv: string,
    expirationDate: string,
    cardholderName: string,
    billingAddress: string,
    firstName: string,
    lastName: string,
    company: string,
    countryName: string,
    countryCodeAlpha2: string,
    countryCodeAlpha3: string,
    countryCodeNumeric: string,
    locality: string,
    postalCode: string,
    region: string,
    streetAddress: string,
    extendedAddress: string,
  };

  export type IOSCardParameters = {
    number: string,
    expirationMonth: string,
    expirationYear: string,
    cardholderName: string,
    billingAddress: {
      postalCode: string,
      streetAddress: string,
      extendedAddress: string,
      locality: string,
      region: string,
      countryName: string,
      countryCodeAlpha2: string,
      countryCodeAlpha3: string,
      countryCodeNumeric: string,
      firstName: string,
      lastName: string,
      company: string,
    },
  };
  };

  export type AndroidCardParameters = CardParameters;
