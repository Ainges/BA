import { Config } from './Config';

export class ConfigService {
  private configPath = 'config.json';

  public async getConfig(): Promise<Config | null> {
    try {
      const response = await fetch(this.configPath);
      const configData = await response.json();

      return configData;
    } catch (error) {
      console.error(error);
    }

    return null;
  }
}
