import {
  SEdge,
  SGraphSchema
} from '../../../src'

const ifeSystemComponent = {
  id: 'ife_system',
  type: 'node:capellacomponent',
  layout: 'vbox',
  position: { x: 100, y: 100 },
  children: [
    {
      id: 'ife_system__label',
      type: 'label:heading',
      text: 'IFE System'
    },
    {
      id: 'manage_audio_and_video_diffusion',
      type: 'node:capellaservice',
      layout: 'vbox',
      children: [
        {
          id: 'manage_audio_and_video_diffusion__label',
          type: 'label:heading',
          text: 'Manage Audio Video Diffusion'
        }
      ]
    }
  ]
}

const passengerComponent = {
  id: 'passenger',
  type: 'node:capellacomponent',
  layout: 'vbox',
  position: { x: 500, y: 50 },
  children: [
    {
      id: 'passenger__label',
      type: 'label:heading',
      text: 'Passenger'
    },
    {
      id: 'listen_to_audio_announcement',
      type: 'node:capellaservice',
      layout: 'vbox',
      children: [
        {
          id: 'listen_to_audio_announcement__label',
          type: 'label:heading',
          text: 'Listen to Audio Accouncement'
        }
      ]
    }
  ]
}

const cabinCrewComponent = {
  id: 'cabin_crew',
  type: 'node:capellacomponent',
  layout: 'vbox',
  position: { x: 100, y: 300 },
  children: [
    {
      id: 'cabin_crew__label',
      type: 'label:heading',
      text: 'Cabin Crew'
    },
    {
      id: 'play_airline-imposed_videos',
      type: 'node:capellaservice',
      layout: 'vbox',
      children: [
        {
          id: 'play_airline-imposed_videos__label',
          type: 'label:heading',
          text: 'Play Airline-Imposed Videos'
        }
      ]
    }
  ]
}

const aircraftComponent = {
  id: 'aircraft',
  type: 'node:capellacomponent',
  layout: 'vbox',
  position: { x: 600, y: 350 },
  children: [
    {
      id: 'aircraft__label',
      type: 'label:heading',
      text: 'Aircraft'
    },
    {
      id: 'play_sound_in_cabin',
      type: 'node:capellaservice',
      layout: 'vbox',
      children: [
        {
          id: 'play_sound_in_cabin__label',
          type: 'label:heading',
          text: 'Play Sound In Cabin'
        }
      ]
    }
  ]
}

const manageVideoAndAudioDiffusion2ListenToAudioAnnouncementEdge = {
  id: 'manageVideoAndAudioDiffusion2ListenToAudioAnnouncementEdge',
  type: 'edge:straight',
  sourceId: ifeSystemComponent.children[1].id,
  targetId: passengerComponent.children[1].id
} as SEdge

const manageVideoAndAudioDiffusion2PlayAirlineImposedVideosEdge = {
  id: 'manageVideoAndAudioDiffusion2PlayAirlineImposedVideosEdge',
  type: 'edge:straight',
  sourceId: ifeSystemComponent.children[1].id,
  targetId: cabinCrewComponent.children[1].id
} as SEdge

const manageVideoAndAudioDiffusion2PlaySoundInCabinEdge = {
  id: 'manageVideoAndAudioDiffusion2PlaySoundInCabinEdge',
  type: 'edge:straight',
  sourceId: ifeSystemComponent.children[1].id,
  targetId: aircraftComponent.children[1].id
} as SEdge

const graph: SGraphSchema = {
  id: 'graph',
  type: 'graph',
  children: [
    ifeSystemComponent,
    passengerComponent,
    cabinCrewComponent,
    aircraftComponent,
    manageVideoAndAudioDiffusion2ListenToAudioAnnouncementEdge,
    manageVideoAndAudioDiffusion2PlayAirlineImposedVideosEdge,
    manageVideoAndAudioDiffusion2PlaySoundInCabinEdge
  ]
}

export default graph